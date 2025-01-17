package org.poo.users;

import org.poo.bank.Bank;

public final class Student implements ServicePlan {
    @Override
    public double calculateCommission(final double amount, final Bank bank,
                                      final String currency) {
        return 0;
    }

    @Override
    public String getPlan() {
        return "student";
    }
}
