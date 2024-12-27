package org.poo.card;

public class CardFactory {
    public enum CardType {
        classic, oneTime
    }

    /**
     * It`s used to create a card based on specific parameters
     * @return exactly what type of card it`s desired
     */

    public static Card createCard(final CardType type, final String cardNumber,
                                  final String status) {
        switch (type) {
            case classic: return new ClassicCard(cardNumber, status);
            case oneTime: return new OneTimeCard(cardNumber, status);
            default: throw new IllegalArgumentException("That card type is not supported");
        }
    }
}
