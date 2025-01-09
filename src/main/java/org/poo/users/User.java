package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.commerciants.Commerciant;
import org.poo.transactions.CardPayment;
import org.poo.transactions.Transactions;
import java.time.LocalDate;
import java.time.Period;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public final class User implements Visitable {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String occupation;
    private ServicePlan servicePlan;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Transactions> history = new ArrayList<>();
    private int transactionsCount;
    private double spentMoney;

    public User(final String firstName, final String lastName, final String email,
                final String birthDate, final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.occupation = occupation;
        if (occupation.equals("student")) {
            servicePlan = ServiceFactory.createService(ServiceFactory.ServiceType.Student);
        } else {
            servicePlan = ServiceFactory.createService(ServiceFactory.ServiceType.Standard);
        }
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

    public int getAge() {
        String[] members = birthDate.split("-");
        int year = Integer.parseInt(members[0]);
        int month = Integer.parseInt(members[1]);
        int day = Integer.parseInt(members[2]);

        LocalDate birthDate = LocalDate.of(year, month, day);
        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears();
    }
}
