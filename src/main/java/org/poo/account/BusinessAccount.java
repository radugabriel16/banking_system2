package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public class BusinessAccount extends Account {
    private User owner;
    private ArrayList<Associate> associates = new ArrayList<>();
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
}
