package org.poo.users;

import org.poo.account.Account;

public interface Visitor {

    /**
     * Visit the user and make the desired changes
     */

    void visit(User user, Account account);
}
