package org.poo.commerciants;

public class CashbackFactory {
    public enum CashbackType {
        NrOfTransactions, SpendingThreshold
    }

    public static Cashback createCashback(CashbackType type) {
        switch (type) {
            case NrOfTransactions: return new NrOfTransactions();
            case SpendingThreshold: return new SpendingThreshold();
            default: throw new IllegalArgumentException("That cashback type is not supported");
        }
    }
}
