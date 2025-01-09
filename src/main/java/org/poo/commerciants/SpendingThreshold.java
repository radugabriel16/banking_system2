package org.poo.commerciants;

import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.users.User;

public class SpendingThreshold implements Cashback {
    @Override
    public void pay(double amount, Commerciant comm, User user, Account account, Bank bank) {
        user.setSpentMoney(user.getSpentMoney() + amount);
        double money = bank.getMoneyConversion().convertMoney(account.getCurrency(), "RON", user.getSpentMoney());
        if (money >= 100 && money < 300) {
            if (user.getServicePlan().getPlan().equals("student") || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = 0.001 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = 0.003 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = 0.005 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
        } else if (money >= 300 && money < 500) {
            if (user.getServicePlan().getPlan().equals("student") || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = 0.002 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = 0.004 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = 0.0055 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
        } else if (money >= 500) {
            if (user.getServicePlan().getPlan().equals("student") || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = 0.0025 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = 0.005 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = 0.007 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
        }
        comm.setAmountReceived(comm.getAmountReceived() + amount);
    }
}
