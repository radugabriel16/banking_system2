package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.transactions.Transactions;
import java.time.LocalDate;
import java.time.Period;

import java.util.ArrayList;

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
    private ArrayList<SplitRequest> requests = new ArrayList<>();

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
     * @return the age of a user based on data extracted from "birthDate" string
     */

    public int getAge() {
        String[] members = birthDate.split("-");
        int year = Integer.parseInt(members[0]);
        int month = Integer.parseInt(members[1]);
        int day = Integer.parseInt(members[2]);

        LocalDate birthTime = LocalDate.of(year, month, day);
        Period period = Period.between(birthTime, LocalDate.now());
        return period.getYears();
    }
}
