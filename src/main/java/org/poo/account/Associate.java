package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Associate {
    private User user;
    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<Deposit> deposits = new ArrayList<>();
    private ArrayList<CommPayment> paymentsToCommerciant = new ArrayList<>();

    public Associate(final User user) {
        this.user = user;
    }

    /**
     * Each type of associate (manager/employee) pays with specific conditions
     */

    public abstract int pay(double amount, BusinessAccount account, int timeStamp,
                            Commerciant comm);

    /**
     * Each type of associate (manager/employee) deposits with specific conditions
     */

    public abstract int deposit(double amount, BusinessAccount account, int timeStamp);

    /**
     * @return the type of associate
     */

    public abstract String getType();

    /**
     * @return the amount spent by associates in a specific interval of time
     */

    public abstract double getSpent(int start, int end);

    /**
     * @return the amount deposited by associates in a specific interval of time
     */

    public abstract double getDeposited(int start, int end);
}
