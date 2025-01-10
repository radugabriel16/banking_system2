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
public class SplitCustom implements Transactions {
    private int timeStamp;
    private String currency;
    private double amount;
    private ArrayList<Double> amountList = new ArrayList<>();
    private ArrayList<Account> accounts = new ArrayList<>();
    private boolean success = true;
    private Bank bank;
    private String notEnoughMoney;
    private String currentIban;
    private int status;

    public SplitCustom(int timeStamp, String currency, double amountTotal, List<Double> amount, Bank bank, List<String> iban,
                       int status) {
        this.timeStamp = timeStamp;
        this.currency = currency;
        this.bank = bank;
        this.amount = amountTotal;
        this.amountList.addAll(amount);
        for (int i = 0; i < iban.size(); i++) {
            accounts.add(bank.findAccount(iban.get(i)));
        }
        this.status = status;
    }

    public SplitCustom(final SplitCustom original) {
        this.timeStamp = original.timeStamp;
        this.currency = original.currency;
        this.amount = original.amount;
        this.amountList.addAll(original.amountList);
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
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i) != null) {
                    String accountCurrency = accounts.get(i).getCurrency();
                    double sum = amountList.get(i);
                    double neededMoney = bank.getMoneyConversion().convertMoney(currency, accountCurrency, sum);
                    if (neededMoney > accounts.get(i).getBalance()) {
                        success = false;
                        notEnoughMoney = accounts.get(i).getIban();
                        break;
                    }
                    amountToBeExtracted.add(neededMoney);
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
    public ObjectNode convertJson(User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        String sum = String.valueOf(amount);
        if (sum.charAt(sum.length() - 1) == '0')
            node.put("description", "Split payment of " + amount + "0 " + currency);
        else
            node.put("description", "Split payment of " + amount + " " + currency);
        node.put("splitPaymentType", "custom");
        node.put("currency", currency);

        ArrayNode amountArray = mapper.createArrayNode();
        for (Double amount : amountList) {
            amountArray.add(amount);
        }
        node.set("amountForUsers", amountArray);
        ArrayNode ibanArray = mapper.createArrayNode();
        for (Account account : accounts) {
            ibanArray.add(account.getIban());
        }
        node.set("involvedAccounts", ibanArray);
        if (!success && status == 1) {
            node.put("error", "Account " + notEnoughMoney + " has insufficient funds for a split "
                    + "payment.");
        } else if (status == 2) {
            node.put("error", "One user rejected the payment");
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
