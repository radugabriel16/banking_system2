package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
import org.poo.exchange_rate.MoneyConversion;
import org.poo.fileio.CommandInput;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.UserInput;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public class Bank implements Visitable {
    private ArrayList<User> bankUsers = new ArrayList<>();
    private MoneyConversion moneyConversion;
    private ArrayList<Commerciant> commerciants = new ArrayList<>();

    public Bank(final UserInput[] users, final MoneyConversion moneyConversion,
                final CommerciantInput[] com) {
        for (UserInput user : users) {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getBirthDate(), user.getOccupation());
            bankUsers.add(newUser);
        }
        this.moneyConversion = moneyConversion;
        for (CommerciantInput commerciant : com) {
            Commerciant newCom = new Commerciant(commerciant.getCommerciant(), commerciant.getId(),
                    commerciant.getAccount(), commerciant.getType(), commerciant.getCashbackStrategy());
            commerciants.add(newCom);
        }
    }

    /**
     * It searches for the owner of an account
     * @param detail represents the iban or alias
     * @return the user
     */

    public User findUser(final String detail) {
        for (User user : bankUsers) {
            if (user.getEmail().equals(detail)) {
                return user;
            }
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(detail)) {
                    return user;
                } else if (account.getAlias() != null) {
                    if (account.getAlias().equals(detail)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * It searches for an account based on email and iban
     * @return the account
     */

    public Account findAccount(final String email, final String iban) {
        User user = findUser(email);
        if (user != null) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * It verifies if an account was linked to an alias
     * @return the truth value
     */

    public boolean searchedAfterAlias(final String alias) {
        for (User user : bankUsers) {
            for (Account account : user.getAccounts()) {
                if (account.getAlias() != null) {
                    if (account.getAlias().equals(alias)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * It searches for an account based on iban or alias
     * @return the account
     */

    public Account findAccount(final String detail) {
        for (User user : bankUsers) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(detail)) {
                    return account;
                } else if (account.getAlias() != null) {
                    if (account.getAlias().equals(detail)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * It searches for the account the card is associated with
     * @return the account
     */

    public Account findParentAccount(final String cardNumber) {
        for (User user : bankUsers) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * It searches for the owner of a specific card
     * @return the user
     */

    public User findOwnerCard(final String cardNumber) {
        for (User user : bankUsers) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    /**
     * It searches for a specific card
     * @return the card
     */

    public Card findCard(final String cardNumber) {
        for (User user : bankUsers) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }

    /**
     * After a payment with the one time card, the method changes the used card with the new created
     * one
     * @param previous the used card for payment
     * @param newCard the new card
     * @param cards the list of card
     */

    public void oneTimeCardChange(final Card previous, final Card newCard,
                                  final ArrayList<Card> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getCardNumber().equals(previous.getCardNumber())) {
                cards.set(i, newCard);
            }
        }
        cards.add(previous);
    }

    /**
     * The money are added in the account
     */

    public void accept(final Visitor visitor, final String iban, final double amount) {
        visitor.visit(this, iban, amount);
    }

    public Commerciant getCommerciant(String name) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant != null) {
                if (commerciant.getName().equals(name)) {
                    return commerciant;
                }
            }
        }
        return null;
    }
}
