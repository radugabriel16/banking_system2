package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.users.User;

@Getter
@Setter
public class Employee extends Associate {
    public Employee(final User user) {
        super(user);
    }

    /**
     * Payment method for an employee
     * It also verifies the spending limit
     */

    @Override
    public int pay(final double amount, final BusinessAccount account, final int timeStamp,
                   final Commerciant comm) {
        if (amount <= account.getSpendingLimit()) {
            account.setTotalSpent(account.getTotalSpent() + amount);
            this.getPayments().add(new Payment(timeStamp, amount));
            this.getPaymentsToCommerciant().add(new CommPayment(timeStamp, amount, comm));
            return 1;
        }
        return 0;
    }

    /**
     * Deposit method for an employee
     * It also verifies the deposit limit
     */

    @Override
    public int deposit(final double amount, final BusinessAccount account, final int timeStamp) {
        if (amount <= account.getDepositLimit()) {
            account.setTotalDeposit(account.getTotalDeposit() + amount);
            this.getDeposits().add(new Deposit(timeStamp, amount));
            return 1;
        }
        return 0;
    }

    /**
     * @return the associate type
     */

    @Override
    public String getType() {
        return "employee";
    }

    /**
     * Calculates the total amount spent by an employee in a specific interval of time
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
     * Calculates the total amount deposited by an employee in a specific interval of time
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

