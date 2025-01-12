package org.poo.bank;

import org.poo.account.Account;
import org.poo.users.User;

public interface Visitable {

    /**
     *  Accepts the `AddFunds` visitor
     */

    void accept(Visitor visitor, Account account, double amount, User user, int timeStamp);
}
