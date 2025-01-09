package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;

@Getter
@Setter
public class Silver implements ServicePlan {
    private int purchases;

    @Override
    public double calculateCommission(double amount, Bank bank, String currency) {
        double amountInRon = bank.getMoneyConversion().convertMoney(currency, "RON", amount);
        if (amountInRon < 500) {
            return 0;
        } else
            return 0.001 * amount;
    }

    @Override
    public String getPlan() {
        return "silver";
    }
}
