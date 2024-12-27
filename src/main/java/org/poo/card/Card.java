package org.poo.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Card {
    private String cardNumber;
    private String status;

    public Card(final String cardNumber, final String status) {
        this.cardNumber = cardNumber;
        this.status = status;
    }

    /**
     * @return the specific type of card
     */

    public String getType() {
        return null;
    }
}
