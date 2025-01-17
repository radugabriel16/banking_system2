package org.poo.commerciants;

import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

public interface Cashback {

    /**
     * Each type of cashback will give money at some point to the account
     */

    void pay(double amount, Commerciant comm, User user, Account account, Bank bank);
}
