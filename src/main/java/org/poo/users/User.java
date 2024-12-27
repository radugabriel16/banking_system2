package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.transactions.CardPayment;
import org.poo.transactions.Transactions;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public final class User implements Visitable {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Transactions> history = new ArrayList<>();

    public User(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * It`s used for deleting an account held by the current user
     */

    public void accept(final Visitor visitor, final Account account) {
        visitor.visit(this, account);
    }

    /**
     * It searches for the iban of an account with a specific alias
     * @return the iban
     */

    public String findIban(final String alias) {
        for (Account account : accounts) {
            if (account.getAlias().equals(alias)) {
                return account.getIban();
            }
        }
        return null;
    }

    /**
     * It deducts the money spent on specific commerciants when the payment transaction was placed
     * outside the interval of time. It`s used for spendings reports to know the amount spent for
     * every one of them
     * @param account representing the history we look at
     * @return the updated list of commerciants
     */

    public ArrayList<Commerciant> editCommerciantsAmounts(final Account account, final int start,
                                                          final int end) {
        ArrayList<Commerciant> current = new ArrayList<>(account.getCommerciants());
        ArrayList<Transactions> transactions = history;
        for (Transactions transaction : transactions) {
            if (transaction.spendingTransaction()) {
                CardPayment payment = (CardPayment) transaction;
                int timeStamp = transaction.getTimestamp();
                if ((timeStamp < start || timeStamp > end) && payment.getIBAN()
                        .equals(account.getIban())) {
                    double amount = payment.getAmount();
                    int index = account.findCommerciant(payment.getCommerciant());
                    if (index != -1) {
                        Commerciant comerciant = current.get(index);
                        comerciant.setAmountReceived(comerciant.getAmountReceived() - amount);
                    }
                }
            }
        }
        Iterator<Commerciant> iterator = current.iterator();
        while (iterator.hasNext()) {
            Commerciant commerciant = iterator.next();

            // It means the only payment at this commerciant was placed outside this interval
            if (commerciant.getAmountReceived() == 0) {
                iterator.remove();
            }
        }
        account.sortCommerciants(current);
        return current;
    }
}
