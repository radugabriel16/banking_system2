package org.poo.account;

import org.poo.users.User;

public class Manager extends Associate {
    public Manager(User user) {
        super(user);
    }

    @Override
    public int pay(double amount, BusinessAccount account, int timeStamp) {
        account.setTotalSpent(account.getTotalSpent() + amount);
        this.getPayments().add(new Payment(timeStamp, amount));
        return 1;
    }

    @Override
    public int deposit(double amount, BusinessAccount account, int timeStamp) {
        account.setTotalDeposit(account.getTotalDeposit() + amount);
        this.getDeposits().add(new Deposit(timeStamp, amount));
        return 1;
    }

    @Override
    public String getType() {
        return "manager";
    }

    @Override
    public double getSpent(int start, int end) {
        double sum = 0;
        for (Payment payment : this.getPayments()) {
            if (payment.getTimeStamp() >= start && payment.getTimeStamp() <= end) {
                sum += payment.getAmount();
            }
        }
        return sum;
    }

    @Override
    public double getDeposited(int start, int end) {
        double sum = 0;
        for (Deposit deposit : this.getDeposits()) {
            if (deposit.getTimeStamp() >= start && deposit.getTimeStamp() <= end) {
                sum += deposit.getAmount();
            }
        }
        return sum;
    }
}
