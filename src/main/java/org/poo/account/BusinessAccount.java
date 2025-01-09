package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public class BusinessAccount extends Account {
    private User owner;
    private ArrayList<User> managers;
    private ArrayList<User> employees;

    public BusinessAccount(final double balance, final String currency, final String iban) {
        super(balance, currency, iban);
    }

    @Override
    public String getType() {
        return "business";
    }
}
