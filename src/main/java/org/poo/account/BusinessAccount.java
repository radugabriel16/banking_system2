package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.users.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BusinessAccount extends Account {
    private User owner;
    private ArrayList<Associate> associates = new ArrayList<>();
    private List<Commerciant> commerciants = new ArrayList<>();
    private double spendingLimit;
    private double depositLimit;
    private double totalSpent;
    private double totalDeposit;

    public BusinessAccount(final double balance, final String currency, final String iban) {
        super(balance, currency, iban);
    }

    /**
     * @return the account type
     */

    @Override
    public String getType() {
        return "business";
    }

    /**
     * @return the associate linked to a given user
     */

    public Associate getAssociate(final User user) {
        for (Associate associate : associates) {
            if (associate.getUser().equals(user)) {
                return associate;
            }
        }
        return null;
    }

    /**
     * @return if a business account already have paid to a specific commerciant
     */

    public boolean existCommerciant(final Associate associate, final Commerciant commerciant) {
        for (CommPayment commPayment : associate.getPaymentsToCommerciant()) {
            if (commPayment.getCommerciant().equals(commerciant)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return all managers who paid to a specific commerciant
     */

    public List<Manager> getManagersInvolved(final Commerciant commerciant) {
        List<Manager> managers = new ArrayList<>();
        for (Associate associate : associates) {
            if (associate.getType().equals("manager")
                    && existCommerciant(associate, commerciant)) {
                Manager m = (Manager) associate;
                managers.add(m);
            }
        }
        return managers;
    }

    /**
     * @return all employees who paid to a specific commerciant
     */

    public List<Employee> getEmployeesInvolved(final Commerciant commerciant) {
        List<Employee> employees = new ArrayList<>();
        for (Associate associate : associates) {
            if (associate.getType().equals("employee")
                    && existCommerciant(associate, commerciant)) {
                Employee e = (Employee) associate;
                employees.add(e);
            }
        }
        return employees;
    }

    /**
     * @return if a user is part of a business account
     */

    public boolean isUserInvolved(final User user) {
        if (owner.equals(user)) {
            return true;
        }
        for (Associate associate : associates) {
            if (associate.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }
}
