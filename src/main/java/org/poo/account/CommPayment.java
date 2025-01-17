package org.poo.account;

import lombok.Getter;
import lombok.Setter;
import org.poo.commerciants.Commerciant;

@Getter
@Setter
public class CommPayment {
    private int timeStamp;
    private double amount;
    private Commerciant commerciant;

    public CommPayment(int timeStamp, double amount, Commerciant commerciant) {
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.commerciant = commerciant;
    }
}
