package org.poo.exchange_rate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRate {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public ExchangeRate(final String from, final String to, final double rate,
                        final int timestamp) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.timestamp = timestamp;
    }
}
