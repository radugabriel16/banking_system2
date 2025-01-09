package org.poo.users;

import org.poo.bank.Bank;

import java.io.Serializable;

public class Standard implements ServicePlan {
    @Override
    public double calculateCommission(double amount, Bank bank, String currency) {
        return 0.002 * amount;
    }

    @Override
    public String getPlan() {
        return "standard";
    }
}
