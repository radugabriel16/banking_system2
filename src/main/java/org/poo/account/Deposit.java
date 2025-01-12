package org.poo.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Deposit {
    private int timeStamp;
    private double amount;

    public Deposit(int timeStamp, double amount) {
        this.timeStamp = timeStamp;
        this.amount = amount;
    }
}
