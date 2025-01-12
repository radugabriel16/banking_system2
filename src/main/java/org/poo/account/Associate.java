package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Associate {
    private User user;
    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<Deposit> deposits = new ArrayList<>();

    public Associate(User user) {
        this.user = user;
    }

    abstract public void pay(double amount, BusinessAccount account, int timeStamp);
    abstract public void deposit(double amount, BusinessAccount account, int timeStamp);
    abstract public String getType();
    abstract public double getSpent(int start, int end);
    abstract public double getDeposited(int start, int end);
}
