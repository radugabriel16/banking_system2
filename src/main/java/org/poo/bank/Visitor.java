package org.poo.bank;

import org.poo.account.Account;
import org.poo.users.User;

public interface Visitor {

    /**
     * Visit the bank and make the desired changes
     */

    void visit(Bank bank, Account account, double amount, User user, int timeStamp);
}
