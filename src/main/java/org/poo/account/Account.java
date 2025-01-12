package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
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
    ArrayList<Discount> discountsAvailable = new ArrayList<>();

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
     * This is used for the spending report, It verifies the existence of a specific commerciant
     * @param commerciantName representing where the buyer spent its money
     * @return the truth value
     */

    /*public boolean commerciantExist(final String commerciantName) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getName().equals(commerciantName)) {
                return true;
            }
        }
        return false;
    }*/

    /**
     * It searches for a specific commerciant
     * @return the index where It was founded
     */

    /*public int findCommerciant(final String commerciantName) {
        for (int i = 0; i < commerciants.size(); i++) {
            if (this.getCommerciants().get(i).getName().equals(commerciantName)) {
                return i;
            }
        }
        return -1;
    }*/

    /**
     * It sorts the list of commerciants alphabetically
     */

    public void sortCommerciants(final ArrayList<Commerciant> list) {
        if (list.size() > 1) {
            list.sort(new SortNames());
        }
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

    public String getType() {
        return null;
    }
}
