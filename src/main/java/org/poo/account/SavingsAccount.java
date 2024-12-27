package org.poo.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final double balance, final String currency, final String iban,
                          final double interestRate) {
        super(balance, currency, iban);
        this.interestRate = interestRate;
    }

    @Override
    public String getType() {
        return "savings";
    }
}
