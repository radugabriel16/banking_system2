package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;

@Getter
@Setter
public final class Silver implements ServicePlan {
    private int purchases;
    private static final int THRESHOLD = 500;
    private static final double FACTOR = 0.001;

    @Override
    public double calculateCommission(final double amount, final Bank bank,
                                      final String currency) {
        double amountInRon = bank.getMoneyConversion().convertMoney(currency, "RON", amount);
        if (amountInRon < THRESHOLD) {
            return 0;
        } else {
            return FACTOR * amount;
        }
    }

    @Override
    public String getPlan() {
        return "silver";
    }
}
