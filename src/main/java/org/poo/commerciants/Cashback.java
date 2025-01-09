package org.poo.commerciants;

import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

public interface Cashback {
    void pay(double amount, Commerciant comm, User user, Account account, Bank bank);
}
