package org.poo.users;

import org.poo.bank.Bank;

public class Gold implements ServicePlan {
    @Override
    public double calculateCommission(double amount, Bank bank, String currency) {
        return 0;
    }

    @Override
    public String getPlan() {
        return "gold";
    }
}
