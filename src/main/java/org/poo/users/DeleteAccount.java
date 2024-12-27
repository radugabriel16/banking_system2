package org.poo.users;

import org.poo.account.Account;

import java.util.ArrayList;

public final class DeleteAccount implements Visitor {

    /**
     * It searches for an account and delete it
     */

    public void visit(final User user, final Account account) {
        ArrayList<Account> accounts = user.getAccounts();
        account.getCards().clear();
        accounts.remove(account);
    }
}
