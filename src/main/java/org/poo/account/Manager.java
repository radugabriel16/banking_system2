package org.poo.account;

import org.poo.commerciants.Commerciant;
import org.poo.users.User;

public class Manager extends Associate {
    public Manager(final User user) {
        super(user);
    }

    /**
     * Payment method for a manager
     */

    @Override
    public int pay(final double amount, final BusinessAccount account, final int timeStamp,
                   final Commerciant comm) {
        account.setTotalSpent(account.getTotalSpent() + amount);
        this.getPayments().add(new Payment(timeStamp, amount));
        this.getPaymentsToCommerciant().add(new CommPayment(timeStamp, amount, comm));
        return 1;
    }

    /**
     * Deposit method for a manager
     */

    @Override
    public int deposit(final double amount, final BusinessAccount account, final int timeStamp) {
        account.setTotalDeposit(account.getTotalDeposit() + amount);
        this.getDeposits().add(new Deposit(timeStamp, amount));
        return 1;
    }

    /**
     * @return the associate type
     */

    @Override
    public String getType() {
        return "manager";
    }

    /**
     * Calculates the total amount spent by a manager in a specific interval of time
     */

    @Override
    public double getSpent(final int start, final int end) {
        double sum = 0;
        for (Payment payment : this.getPayments()) {
            if (payment.getTimeStamp() >= start && payment.getTimeStamp() <= end) {
                sum += payment.getAmount();
            }
        }
        return sum;
    }

    /**
     * Calculates the total amount deposited by a manager in a specific interval of time
     */

    @Override
    public double getDeposited(final int start, final int end) {
        double sum = 0;
        for (Deposit deposit : this.getDeposits()) {
            if (deposit.getTimeStamp() >= start && deposit.getTimeStamp() <= end) {
                sum += deposit.getAmount();
            }
        }
        return sum;
    }
}
