package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public class ExtractMoney implements Transactions {
    private int timeStamp;
    private String currency;
    private double amount;
    private String iban;
    private int messageType;
    private Bank bank;

    public ExtractMoney(int timeStamp, String currency, double amount, String iban, Bank bank) {
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
            messageType = 1;
            return;
        } else if (!account.getType().equals("savings")) {
            messageType = 2;
            return;
        }

        User user = bank.findUser(iban);
        if (user.getAge() < 21) {
            messageType = 3;
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
            messageType = 4;
            return;
        }

        double newAmount = amount;
        newAmount += user.getServicePlan().calculateCommission(amount, bank, currency);
        if (newAmount > account.getBalance()) {
            messageType = 5;
            return;
        }

        account.setBalance(account.getBalance() - newAmount);
        firstClassicAccount.setBalance(firstClassicAccount.getBalance() + amount);
    }

    @Override
    public ObjectNode convertJson(User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == 1)
            node.put("description", "Account not found");
        else if (messageType == 2)
            node.put("description", "Account is not of type savings.");
        else if (messageType == 3)
            node.put("description", "You don't have the minimum age required.");
        else if (messageType == 4)
            node.put("description", "You do not have a classic account.");
        else if (messageType == 5)
            node.put("description", "Insufficient funds");
        else
            node.put("description", "Savings withdrawal");
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
