package org.poo.card;

public final class OneTimeCard extends Card {
    public OneTimeCard(final String cardNumber, final String status) {
        super(cardNumber, status);
    }

    @Override
    public String getType() {
        return "oneTime";
    }
}
