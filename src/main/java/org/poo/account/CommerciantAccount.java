package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.commerciants.Commerciant;

@Getter
@Setter
public class CommerciantAccount extends Account {
    private Bank bank;

    public CommerciantAccount(final double balance, final String currency, final String iban,
                              final Bank bank) {
        super(balance, currency, iban);
        this.bank = bank;
    }

    /**
     * @return the account type
     */

    @Override
    public String getType() {
        return "commerciant";
    }

    /**
     * @return the commerciant whose account is this object
     */

    public Commerciant getCommerciant() {
        for (Commerciant c : getBank().getCommerciants()) {
            if (c.getAccount().equals(this)) {
                return c;
            }
        }
        return null;
    }
}
