package org.poo.users;

import org.poo.account.Account;

public interface Visitable {

    /**
     *  Accepts the `DeleteAccount` visitor
     */

    void accept(Visitor visitor, Account account);
}
