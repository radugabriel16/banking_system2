package org.poo.account;

public class AccountFactory {
    public enum AccountType {
        classic, savings, business
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
            default: throw new IllegalArgumentException("That account type is not supported");
        }
    }
}
