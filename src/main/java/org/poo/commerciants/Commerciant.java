package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;

import java.util.Calendar;

@Getter
@Setter
public class Commerciant {
    private String name;
    private int id;
    private Account account;
    private String type;
    private Cashback cashback;
    private double amountReceived;

    public Commerciant(final String name, final int id, final Account account, final String type,
                       final String cashbackType) {
        this.name = name;
        this.id = id;
        this.account = account;
        this.type = type;
        if (cashbackType.equals("nrOfTransactions"))
            cashback = CashbackFactory.createCashback(CashbackFactory.CashbackType.NrOfTransactions);
        else
            cashback = CashbackFactory.createCashback(CashbackFactory.CashbackType.SpendingThreshold);
    }
}
