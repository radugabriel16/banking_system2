package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.commerciants.Cashback;
import org.poo.commerciants.CashbackFactory;
import org.poo.commerciants.Commerciant;

@Getter
@Setter
public class CommerciantAccount extends Account {
    private Bank bank;

    public CommerciantAccount(double balance, String currency, String iban, Bank bank) {
        super(balance, currency, iban);
        this.bank = bank;
    }

    @Override
    public String getType() {
        return "commerciant";
    }

    public Commerciant getCommerciant() {
        for (Commerciant c : getBank().getCommerciants()) {
            if (c.getAccount().equals(this)) {
                return c;
            }
        }
        return null;
    }
}
