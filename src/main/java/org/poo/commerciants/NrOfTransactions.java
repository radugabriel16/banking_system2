package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

@Getter
@Setter
public class NrOfTransactions implements Cashback {
    @Override
    public void pay(double amount, Commerciant comm, User user, Account account, Bank bank) {
        String typeCommerciant = comm.getType();
        Discount discount = account.findDiscount(typeCommerciant);
        if (discount != null) {
            double cashback = discount.getCashbackPercent() / 100 * amount;
            account.setBalance(account.getBalance() + cashback);
            account.getDiscountsAvailable().remove(discount);
        }
        comm.setAmountReceived(comm.getAmountReceived() + amount);
        account.setTransactionsCount(account.getTransactionsCount() + 1);
        if (account.getTransactionsCount() == 2) {
            account.getDiscountsAvailable().add(new Discount("Food", 2));
        } else if (account.getTransactionsCount() == 5) {
            account.getDiscountsAvailable().add(new Discount("Clothes", 5));
        } else if (account.getTransactionsCount() == 10) {
            account.getDiscountsAvailable().add(new Discount("Tech", 10));
        }
    }
}
