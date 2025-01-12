package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.*;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
import org.poo.users.ServiceFactory;
import org.poo.users.ServicePlan;
import org.poo.users.Silver;
import org.poo.users.User;

@Getter
@Setter
public final class CardPayment implements Transactions {
    private String cardNumber;
    private double amount;
    private String currency;
    private String description;
    private String commerciant;
    private User user;
    private Bank bank;
    private int timeStamp;
    private boolean success = false;
    private String status;
    private String iban;
    private boolean oneTimeCard = false;

    public CardPayment(final String cardNumber, final double amount, final String currency,
                       final String description, final String commerciant, final Bank bank,
                       final User user, final int timeStamp, final String status,
                       final String iban) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.bank = bank;
        this.user = user;
        this.timeStamp = timeStamp;
        this.status = status;
        this.iban = iban;
    }

    @Override
    public void execute() {
        User potentialBuyer = bank.findOwnerCard(cardNumber);
        Account account = bank.findParentAccount(cardNumber);
        Card card = bank.findCard(cardNumber);
        if (account != null && card.getStatus().equals("active")) {
            amount = bank.getMoneyConversion().convertMoney(currency, account.getCurrency(),
                    amount);
            double newAmount = amount;
            newAmount += potentialBuyer.getServicePlan().calculateCommission(newAmount, bank, currency);

            if (account.getBalance() >= newAmount) {
                double amountInRon = bank.getMoneyConversion().convertMoney(account.getCurrency(), "RON", amount);
                if (amountInRon > 300 && potentialBuyer.getServicePlan().getPlan().equals("silver")) {
                    Silver silverPlan = (Silver)potentialBuyer.getServicePlan();
                    silverPlan.setPurchases(silverPlan.getPurchases() + 1);
                    if (silverPlan.getPurchases() == 5) { // upgrade la gold
                        ServicePlan newPlan = ServiceFactory.createService(ServiceFactory.ServiceType.Gold);
                        potentialBuyer.setServicePlan(newPlan);
                    }
                }
                if (card.getType().equals("oneTime")) {
                    oneTimeCard = true;
                }

                if (account.getType().equals("business")) {
                    BusinessAccount businessAccount = (BusinessAccount)account;

                    Associate associate = businessAccount.getAssociate(user);
                    if (associate != null) {
                        int result = associate.pay(amount, businessAccount, timeStamp);
                        if (result == 0)
                            return;
                    }
                }

                Commerciant com = bank.getCommerciant(commerciant);
                com.getCashback().pay(amount, com, user, account, bank);

                if (account.getPayments().get(commerciant) != null) {
                    double currentSpent = account.getPayments().get(commerciant);
                    account.getPayments().put(commerciant, currentSpent + amount);
                } else {
                    account.getPayments().put(commerciant, amount);
                }

                account.setBalance(account.getBalance() - newAmount);
                success = true;
            }
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);

        if (status.equals("frozen")) {
            node.put("description", "The card is frozen");
        } else if (!success) {
            node.put("description", "Insufficient funds");
        } else {
            node.put("description", "Card payment");
            node.put("amount", amount);
            node.put("commerciant", commerciant);
        }
        return node;
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }

    @Override
    public boolean spendingTransaction() {
        return true;
    }

    @Override
    public String getIBAN() {
        return iban;
    }
}
