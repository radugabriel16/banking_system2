package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public final class ExtractMoney implements Transactions {
    private int timeStamp;
    private String currency;
    private double amount;
    private String iban;
    private String classicIban;
    private int messageType;
    private Bank bank;
    private int doubleMessage;
    private static final int MESSAGE1 = 1;
    private static final int MESSAGE2 = 2;
    private static final int MESSAGE3 = 3;
    private static final int MESSAGE4 = 4;
    private static final int MESSAGE5 = 5;
    private static final int MINIMUM_AGE = 21;

    public ExtractMoney(final int timeStamp, final String currency, final double amount,
                        final String iban, final Bank bank) {
        this.timeStamp = timeStamp;
        this.currency = currency;
        this.amount = amount;
        this.iban = iban;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Account account = bank.findAccount(iban);
        if (account == null) {
            messageType = MESSAGE1;
            return;
        } else if (!account.getType().equals("savings")) {
            messageType = MESSAGE2;
            return;
        }

        User user = bank.findUser(iban);
        if (user.getAge() < MINIMUM_AGE) {
            messageType = MESSAGE3;
            return;
        }

        boolean existClassic = false;
        Account firstClassicAccount = null;
        ArrayList<Account> accounts = user.getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getType().equals("classic")) {
                existClassic = true;
                if (accounts.get(i).getCurrency().equals(currency)) {
                    firstClassicAccount = accounts.get(i);
                }
            }
        }
        if (!existClassic || firstClassicAccount == null) {
            messageType = MESSAGE4;
            return;
        }

        if (amount > account.getBalance()) {
            messageType = MESSAGE5;
            return;
        }

        doubleMessage = 1;
        account.setBalance(account.getBalance() - amount);
        firstClassicAccount.setBalance(firstClassicAccount.getBalance() + amount);
        classicIban = firstClassicAccount.getIban();
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == MESSAGE1) {
            node.put("description", "Account not found");
        } else if (messageType == MESSAGE2) {
            node.put("description", "Account is not of type savings.");
        } else if (messageType == MESSAGE3) {
            node.put("description", "You don't have the minimum age required.");
        } else if (messageType == MESSAGE4) {
            node.put("description", "You do not have a classic account.");
        } else if (messageType == MESSAGE5) {
            node.put("description", "Insufficient funds");
        } else {
            node.put("description", "Savings withdrawal");
            node.put("savingsAccountIBAN", iban);
            node.put("classicAccountIBAN", classicIban);
            node.put("amount", amount);
        }
        return node;
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }

    @Override
    public boolean spendingTransaction() {
        return false;
    }

    @Override
    public String getIBAN() {
        return iban;
    }
}
