package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Discount {
    private String type;
    private double cashbackPercent;

    public Discount(String type, double cashbackPercent) {
        this.type = type;
        this.cashbackPercent = cashbackPercent;
    }
}
