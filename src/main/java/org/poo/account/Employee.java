package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public class Employee extends Associate {
    public Employee (User user) {
        super(user);
    }

    @Override
    public void pay(double amount, BusinessAccount account, int timeStamp) {
        if (amount <= account.getSpendingLimit()) {
            account.setTotalSpent(account.getTotalSpent() + amount);
            this.getPayments().add(new Payment(timeStamp, amount));
        }
    }

    @Override
    public void deposit(double amount, BusinessAccount account, int timeStamp) {
        if (amount <= account.getDepositLimit()) {
            account.setTotalDeposit(account.getTotalDeposit() + amount);
            this.getDeposits().add(new Deposit(timeStamp, amount));
        }
    }

    @Override
    public String getType() {
        return "employee";
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
