package org.poo.commerciants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Commerciant {
    private String name;
    private double amountReceived;

    public Commerciant(final String name, final double amountReceived) {
        this.name = name;
        this.amountReceived = amountReceived;
    }
}
