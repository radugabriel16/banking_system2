package org.poo.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {
    private int timeStamp;
    private double amount;

    public Payment(final int timeStamp, final double amount) {
        this.timeStamp = timeStamp;
        this.amount = amount;
    }
}
