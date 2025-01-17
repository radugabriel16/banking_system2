package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class SplitPayment implements Transactions {
    private int timeStamp;
    private String currency;
    private double amountPerPerson;
    private ArrayList<Account> accounts = new ArrayList<>();
    private boolean success = true;
    private Bank bank;
    private String notEnoughMoney;
    private String currentIban;
    private int status;

    public SplitPayment(final int timeStamp, final String currency, final double amount,
                        final Bank bank, final List<String> iban, final int status) {
        this.timeStamp = timeStamp;
        this.currency = currency;
        this.amountPerPerson = amount / iban.size();
        this.bank = bank;
        for (int i = 0; i < iban.size(); i++) {
            accounts.add(bank.findAccount(iban.get(i)));
        }
        this.status = status;
    }

    public SplitPayment(final SplitPayment original) {
        this.timeStamp = original.timeStamp;
        this.currency = original.currency;
        this.amountPerPerson = original.amountPerPerson;
        this.bank = original.bank;
        this.success = original.success;
        this.notEnoughMoney = original.notEnoughMoney;
        this.currentIban = original.currentIban;
        this.accounts = original.accounts;
        this.status = original.status;
    }

    @Override
    public void execute() {
        if (status == 1) {
            ArrayList<Double> amountToBeExtracted = new ArrayList<>();
            for (Account account : accounts) {
                if (account != null) {
                    double specificAmount = bank.getMoneyConversion().convertMoney(currency,
                            account.getCurrency(), amountPerPerson);
                    if (specificAmount > account.getBalance()) {
                        success = false;
                        notEnoughMoney = account.getIban();
                        break;
                    }
                    amountToBeExtracted.add(specificAmount);
                }
            }

            // The payment is valid and money are deducted from every account
            if (success) {
                for (int i = 0; i < accounts.size(); i++) {
                    accounts.get(i).setBalance(accounts.get(i).getBalance()
                            - amountToBeExtracted.get(i));
                }
            }
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        double amount = amountPerPerson * accounts.size();
        String sum = String.valueOf(amount);
        if (sum.charAt(sum.length() - 1) == '0' || sum.charAt(sum.length() - 2) == '.') {
            node.put("description", "Split payment of " + amount + "0" + " " + currency);
        } else {
            node.put("description", "Split payment of " + amount + " " + currency);
        }
        node.put("splitPaymentType", "equal");
        node.put("currency", currency);
        node.put("amount", amountPerPerson);

        ArrayNode involved  = mapper.createArrayNode();
        for (Account account : accounts) {
            involved.add(account.getIban());
        }
        node.set("involvedAccounts", involved);
        if (!success && status == 1) {
            node.put("error", "Account " + notEnoughMoney + " has insufficient funds for a split "
                    + "payment.");
        } else if (status == 2) {
            node.put("error", "One user rejected the payment.");
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
        return currentIban;
    }
}
