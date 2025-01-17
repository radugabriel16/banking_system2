package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.card.Card;
import org.poo.commerciants.Discount;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class Account {
    private double balance;
    private String currency;
    private String iban;
    private ArrayList<Card> cards = new ArrayList<>();
    private String alias;
    private double minBalance;
    private HashMap<String, Double> payments = new HashMap<>();
    private int transactionsCount;
    private double spentMoney;
    private ArrayList<Discount> discountsAvailable = new ArrayList<>();

    public Account(final double balance, final String currency, final String iban) {
        this.balance = balance;
        this.currency = currency;
        this.iban = iban;
    }

    /**
     * It finds a specific card
     * @param cardNumber representing the search criteria
     * @return the desired card
     */

    public Card findCard(final String cardNumber) {
        for (Card card : getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * Used to freeze the cards of an account which has problems with money
     */

    public void blockCards() {
        for (Card card : getCards()) {
            card.setStatus("frozen");
        }
    }

    /**
     * This method it`s overridden in the subclasses to get the type of account
     * @return a string which represents the type
     */

    public Discount findDiscount(final String discountName) {
        for (Discount discount : getDiscountsAvailable()) {
            if (discount.getType().equals(discountName)) {
                return discount;
            }
        }
        return null;
    }

    /**
     * @return the type of account
     */

    public String getType() {
        return null;
    }
}
