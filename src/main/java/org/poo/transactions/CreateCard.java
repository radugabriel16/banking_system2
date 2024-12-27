package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.card.CardFactory;
import org.poo.users.User;
import org.poo.utils.Utils;

@Getter
@Setter
public final class CreateCard implements Transactions {
    private Account account;
    private String cardNumber;
    private String cardType;
    private int timeStamp;
    private boolean needToChangeOneTime;
    private Bank bank;
    private Card previous;
    private ControlTransactions control;

    public CreateCard(final Account account, final String cardType, final int timeStamp,
                      final boolean needToChangeOneTime, final Bank bank, final Card previous,
                      final ControlTransactions control) {
        this.account = account;
        this.cardType = cardType;
        this.timeStamp = timeStamp;
        this.needToChangeOneTime = needToChangeOneTime;
        this.bank = bank;
        this.previous = previous;
        this.control = control;
    }

    @Override
    public void execute() {
        cardNumber = Utils.generateCardNumber();
        Card card;
        if (cardType.equals("classic")) {
            card = CardFactory.createCard(CardFactory.CardType.classic, cardNumber, "active");
            account.getCards().add(card);
        } else if (cardType.equals("oneTime") && !needToChangeOneTime) {
            card = CardFactory.createCard(CardFactory.CardType.oneTime, cardNumber, "active");
            account.getCards().add(card);
        } else { // Changing the old card with a new `one time card`
            card = CardFactory.createCard(CardFactory.CardType.oneTime, cardNumber, "active");
            bank.oneTimeCardChange(previous, card, account.getCards());
            DeleteCard delete = new DeleteCard(previous.getCardNumber(), account, timeStamp);
            User user = bank.findOwnerCard(cardNumber);

            // Adding a `DeleteCard` transaction
            control.edit(delete, user);

            // Switching the last two transactions to have `delete` before building a new card
            Transactions aux = user.getHistory().getLast();
            Transactions toSwitch = user.getHistory().get(user.getHistory().size() - 2);
            user.getHistory().set(user.getHistory().size() - 1, toSwitch);
            user.getHistory().set(user.getHistory().size() - 2, aux);
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "New card created");
        node.put("card", cardNumber);
        node.put("cardHolder", user.getEmail());
        node.put("account", account.getIban());
        return node;
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }

    @Override
    public boolean spendingTransaction() {
        return false;
    }

    @Override
    public String getIBAN() {
        return account.getIban();
    }
}
