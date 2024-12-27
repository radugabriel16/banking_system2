package org.poo.bank;

import org.poo.account.Account;
import org.poo.users.User;

import java.util.ArrayList;

public class AddFunds implements Visitor {

    /**
     * It searches a specific account and add money to it
     */

    public void visit(final Bank bank, final String iban, final double amount) {
        ArrayList<User> usersToSearch = bank.getBankUsers();
        for (User user : usersToSearch) {
            for (Account account : user.getAccounts()) {
                if (iban.equals(account.getIban())) {
                    account.setBalance(account.getBalance() + amount);
                }
            }
        }
    }
}
