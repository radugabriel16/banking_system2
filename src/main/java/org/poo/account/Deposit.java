package org.poo.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Deposit {
    private int timeStamp;
    private double amount;

    public Deposit(final int timeStamp, final double amount) {
        this.timeStamp = timeStamp;
        this.amount = amount;
    }
}
