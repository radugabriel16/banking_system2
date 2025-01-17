package org.poo.account;

import org.poo.bank.Bank;

public class AccountFactory {
    public enum AccountType {
        classic, savings, business, commerciant
    }

    /**
     * It`s used to create an account based on specific parameters
     * @return exactly what type of account it`s desired
     */

    public static Account createAccount(final AccountType type, final double balance,
                                        final String currency, final String iban,
                                        final Object... params) {
        switch (type) {
            case classic: return new ClassicAccount(balance, currency, iban);
            case savings: return new SavingsAccount(balance, currency, iban, (double) params[0]);
            case business: return new BusinessAccount(balance, currency, iban);
            case commerciant: return new CommerciantAccount(balance, currency, iban,
                    (Bank) params[0]);
            default: throw new IllegalArgumentException("That account type is not supported");
        }
    }
}
