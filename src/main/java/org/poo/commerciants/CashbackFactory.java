package org.poo.commerciants;

public class CashbackFactory {
    public enum CashbackType {
        NrOfTransactions, SpendingThreshold
    }

    /**
     * @return the desired type of cashback based on a given type
     */

    public static Cashback createCashback(final CashbackType type) {
        switch (type) {
            case NrOfTransactions: return new NrOfTransactions();
            case SpendingThreshold: return new SpendingThreshold();
            default: throw new IllegalArgumentException("That cashback type is not supported");
        }
    }
}
