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
        if (user.getTransactionsCount() == 2 && comm.getType().equals("Food")) {
            double cashback = 0.02 * amount;
            account.setBalance(account.getBalance() + cashback);
        } else if (user.getTransactionsCount() == 5 && comm.getType().equals("Clothes")) {
            double cashback = 0.05 * amount;
            account.setBalance(account.getBalance() + cashback);
        } else if (user.getTransactionsCount() == 10 && comm.getType().equals("Tech")) {
            double cashback = 0.1 * amount;
            account.setBalance(account.getBalance() + cashback);
        }
        comm.setAmountReceived(comm.getAmountReceived() + amount);
        user.setTransactionsCount(user.getTransactionsCount() + 1);
    }
}
