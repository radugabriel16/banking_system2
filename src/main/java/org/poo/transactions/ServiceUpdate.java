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
public final class ServiceUpdate implements Transactions {
    private int timeStamp;
    private String planType;
    private String iban;
    private Bank bank;
    private int messageType;
    private double priceToUpgrade;
    private static final int MESSAGE1 = 1;
    private static final int MESSAGE2 = 2;
    private static final int MESSAGE3 = 3;
    private static final int MESSAGE4 = 4;

    public ServiceUpdate(final int timeStamp, final String planType, final String iban,
                         final Bank bank) {
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
            messageType = MESSAGE1;
            return;
        }
        String currentPlan = user.getServicePlan().getPlan();
        if (planType.equals(currentPlan)) {
            messageType = MESSAGE2;
            return;
        } else if (planType.equals("standard") || planType.equals("student")) {
            if (currentPlan.equals("silver") || currentPlan.equals("gold")) {
                messageType = MESSAGE3;
                return;
            }
        } else if (planType.equals("silver")) {
            if (currentPlan.equals("gold")) {
                messageType = MESSAGE3;
                return;
            } else {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(),
                        100);
                if (price > account.getBalance()) {
                    messageType = MESSAGE4;
                    return;
                } else {
                    priceToUpgrade = price;
                }
            }
        } else if (planType.equals("gold")) {
            if (currentPlan.equals("silver")) {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(),
                        250);
                if (price > account.getBalance()) {
                    messageType = MESSAGE4;
                    return;
                } else {
                    priceToUpgrade = price;
                }
            } else {
                double price = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(),
                        350);
                if (price > account.getBalance()) {
                    messageType = MESSAGE4;
                    return;
                } else {
                    priceToUpgrade = price;
                }
            }
        }
        account.setBalance(account.getBalance() - priceToUpgrade);
        if (planType.equals("silver")) {
            user.setServicePlan(ServiceFactory.createService(ServiceFactory.ServiceType.Silver));
        } else if (planType.equals("gold")) {
            user.setServicePlan(ServiceFactory.createService(ServiceFactory.ServiceType.Gold));
        } else if (planType.equals("student")) {
            user.setServicePlan(ServiceFactory.createService(ServiceFactory.ServiceType.Student));
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == 0) {
            node.put("description", "Upgrade plan");
        } else if (messageType == MESSAGE1) {
            node.put("description", "Account not found");
        } else if (messageType == MESSAGE2) {
            node.put("description", "The user already has the " + planType + " plan.");
            return node;
        } else if (messageType == MESSAGE3) {
            node.put("description", "You cannot downgrade your plan.");
            return node;
        } else if (messageType == MESSAGE4) {
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
