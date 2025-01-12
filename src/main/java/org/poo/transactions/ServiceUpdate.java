package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.ServiceFactory;
import org.poo.users.User;

@Getter
@Setter
public class ServiceUpdate implements Transactions {
    private int timeStamp;
    private String planType;
    private String iban;
    private Bank bank;
    private int messageType;
    private double priceToUpgrade;

    public ServiceUpdate(int timeStamp, String planType, String iban, Bank bank) {
        this.timeStamp = timeStamp;
        this.planType = planType;
        this.iban = iban;
        this.bank = bank;
    }

    @Override
    public void execute() {
        User user = bank.findUser(iban);
        Account account = bank.findAccount(iban);
        if (account == null) {
            messageType = 1;
            return;
        }
        String currentPlan = user.getServicePlan().getPlan();
        if (planType.equals(currentPlan)) {
            messageType = 2;
            return;
        } else if (planType.equals("standard") || planType.equals("student")) {
            if (currentPlan.equals("silver") || currentPlan.equals("gold")) {
                messageType = 3;
                return;
            }
        } else if (planType.equals("silver")) {
            if (currentPlan.equals("gold")) {
                messageType = 3;
                return;
            } else {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(), 100);
                if (price > account.getBalance()) {
                    messageType = 4;
                    return;
                } else
                    priceToUpgrade = price;
            }
        } else if (planType.equals("gold")) {
            if (currentPlan.equals("silver")) {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(), 250);
                if (price > account.getBalance()) {
                    messageType = 4;
                    return;
                } else
                    priceToUpgrade = price;
            } else {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(), 350);
                if (price > account.getBalance()) {
                    messageType = 4;
                    return;
                } else
                    priceToUpgrade = price;
            }
        }
        account.setBalance(account.getBalance() - priceToUpgrade);
        if (planType.equals("silver"))
            user.setServicePlan(ServiceFactory.createService(ServiceFactory.ServiceType.Silver));
        else
            user.setServicePlan(ServiceFactory.createService(ServiceFactory.ServiceType.Gold));
    }

    @Override
    public ObjectNode convertJson(User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == 0) {
            node.put("description", "Upgrade plan");
        } else if (messageType == 1) {
            node.put("description", "Account not found");
        } else if (messageType == 2) {
            node.put("description", "The user already has the " + planType + "plan.");
        } else if (messageType == 3) {
            node.put("description", "You cannot downgrade your plan.");
        } else if (messageType == 4) {
            node.put("description", "Insufficient funds");
            return node;
        }
        node.put("accountIBAN", iban);
        node.put("newPlanType", planType);
        return node;
    }

    @Override
    public String getIBAN() {
        return iban;
    }

    @Override
    public boolean spendingTransaction() {
        return false;
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }
}
