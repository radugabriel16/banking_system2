package org.poo.account;

public final class ClassicAccount extends Account {
    public ClassicAccount(final double balance, final String currency, final String iban) {
        super(balance, currency, iban);
    }

    @Override
    public String getType() {
        return "classic";
    }
}
