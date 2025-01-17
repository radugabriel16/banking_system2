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

    @Override
    public String getType() {
        return "business";
    }

    public Associate getAssociate(User user) {
        for (Associate associate : associates) {
            if (associate.getUser().equals(user)) {
                return associate;
            }
        }
        return null;
    }

    public boolean existCommerciant(Associate associate, Commerciant commerciant) {
        for (CommPayment commPayment : associate.getPaymentsToCommerciant()) {
            if (commPayment.getCommerciant().equals(commerciant)) {
                return true;
            }
        }
        return false;
    }

    public List<Manager> getManagersInvolved(Commerciant commerciant) {
        List<Manager> managers = new ArrayList<>();
        for (Associate associate : associates) {
            if (associate.getType().equals("manager") && existCommerciant(associate, commerciant)) {
                Manager m = (Manager) associate;
                managers.add(m);
            }
        }
        return managers;
    }

    public List<Employee> getEmployeesInvolved(Commerciant commerciant) {
        List<Employee> employees = new ArrayList<>();
        for (Associate associate : associates) {
            if (associate.getType().equals("employee") && existCommerciant(associate, commerciant)) {
                Employee e = (Employee) associate;
                employees.add(e);
            }
        }
        return employees;
    }
}
