package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

@Getter
@Setter
public final class NrOfTransactions implements Cashback {
    private static final int CONSTANT = 100;
    private static final int FIRST = 2;
    private static final int SECOND = 5;
    private static final int THIRD = 10;

    @Override
    public void pay(final double amount, final Commerciant comm, final User user,
                    final Account account, final Bank bank) {
        String typeCommerciant = comm.getType();
        Discount discount = account.findDiscount(typeCommerciant);
        if (discount != null) {
            double cashback = discount.getCashbackPercent() / CONSTANT * amount;
            account.setBalance(account.getBalance() + cashback);
            account.getDiscountsAvailable().remove(discount);
        }
        comm.setAmountReceived(comm.getAmountReceived() + amount);
        account.setTransactionsCount(account.getTransactionsCount() + 1);
        if (account.getTransactionsCount() == FIRST) {
            account.getDiscountsAvailable().add(new Discount("Food", FIRST));
        } else if (account.getTransactionsCount() == SECOND) {
            account.getDiscountsAvailable().add(new Discount("Clothes", SECOND));
        } else if (account.getTransactionsCount() == THIRD) {
            account.getDiscountsAvailable().add(new Discount("Tech", THIRD));
        }
    }
}
