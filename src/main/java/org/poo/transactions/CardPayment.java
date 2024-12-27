package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
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
            if (potentialBuyer.equals(user) && account.getBalance() >= amount) {
                if (card.getType().equals("oneTime")) {
                    oneTimeCard = true;
                }

                // Paying for the first time to this commerciant or not
                if (!account.commerciantExist(commerciant)) {
                    account.getCommerciants().add(new Commerciant(commerciant, amount));
                } else {
                    int foundIndex = account.findCommerciant(commerciant);
                    Commerciant found = account.getCommerciants().get(foundIndex);
                    found.setAmountReceived(found.getAmountReceived() + amount);
                }
                account.setBalance(account.getBalance() - amount);
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
