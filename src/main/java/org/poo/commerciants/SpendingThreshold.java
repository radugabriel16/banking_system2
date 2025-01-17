package org.poo.commerciants;

import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.exchange_rate.MoneyConversion;
import org.poo.users.User;

public final class SpendingThreshold implements Cashback {
    private static final int FIRST = 100;
    private static final int SECOND = 300;
    private static final int THIRD = 500;
    private static final double FACTOR1 = 0.001;
    private static final double FACTOR2 = 0.002;
    private static final double FACTOR3 = 0.003;
    private static final double FACTOR4 = 0.004;
    private static final double FACTOR5 = 0.005;
    private static final double FACTOR6 = 0.0055;
    private static final double FACTOR7 = 0.0025;
    private static final double FACTOR8 = 0.007;

    @Override
    public void pay(final double amount, final Commerciant comm, final User user,
                    final Account account, final Bank bank) {
        account.setSpentMoney(account.getSpentMoney() + amount);
        double money = bank.getMoneyConversion().convertMoney(account.getCurrency(), "RON",
                account.getSpentMoney());
        if (money >= FIRST && money < SECOND) {
            if (user.getServicePlan().getPlan().equals("student")
                    || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = FACTOR1 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = FACTOR3 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = FACTOR5 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
            MoneyConversion conversion = bank.getMoneyConversion();
            double extract = conversion.convertMoney("RON", account.getCurrency(), FIRST);
            account.setSpentMoney(account.getSpentMoney() - extract);
        } else if (money >= SECOND && money < THIRD) {
            if (user.getServicePlan().getPlan().equals("student")
                    || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = FACTOR2 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = FACTOR4 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = FACTOR6 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
            MoneyConversion conversion = bank.getMoneyConversion();
            double extract = conversion.convertMoney("RON", account.getCurrency(), SECOND);
            account.setSpentMoney(account.getSpentMoney() - extract);
        } else if (money >= THIRD) {
            if (user.getServicePlan().getPlan().equals("student")
                    || user.getServicePlan().getPlan().equals("standard")) {
                double cashback = FACTOR7 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("silver")) {
                double cashback = FACTOR5 * amount;
                account.setBalance(account.getBalance() + cashback);
            } else if (user.getServicePlan().getPlan().equals("gold")) {
                double cashback = FACTOR8 * amount;
                account.setBalance(account.getBalance() + cashback);
            }
            MoneyConversion conversion = bank.getMoneyConversion();
            double extract = conversion.convertMoney("RON", account.getCurrency(), THIRD);
            account.setSpentMoney(account.getSpentMoney() - extract);
        }
        comm.setAmountReceived(comm.getAmountReceived() + amount);
    }
}
