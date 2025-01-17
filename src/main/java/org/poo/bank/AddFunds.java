package org.poo.bank;

import org.poo.account.*;
import org.poo.users.User;

public class AddFunds implements Visitor {

    /**
     * It searches a specific account and add money to it
     */

    public void visit(final Bank bank, final Account account, final double amount,
                      final User moneyDeliver, final int timeStamp) {
        if (account.getType().equals("business")) {
            BusinessAccount businessAccount = (BusinessAccount) account;
            Associate associate = businessAccount.getAssociate(moneyDeliver);
            if (associate != null) {
                int result = associate.deposit(amount, businessAccount, timeStamp);
                if (result == 0) {
                    return;
                }
            }
        }
        account.setBalance(account.getBalance() + amount);
    }
}
