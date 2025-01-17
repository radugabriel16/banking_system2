package org.poo.users;

import org.poo.bank.Bank;

public final class Standard implements ServicePlan {
    private static final double FACTOR = 0.002;

    @Override
    public double calculateCommission(final double amount, final Bank bank,
                                      final String currency) {
        return FACTOR * amount;
    }

    @Override
    public String getPlan() {
        return "standard";
    }
}
