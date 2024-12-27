package org.poo.card;

public final class ClassicCard extends Card {
    public ClassicCard(final String cardNumber, final String status) {
        super(cardNumber, status);
    }

    @Override
    public String getType() {
        return "classic";
    }
}
