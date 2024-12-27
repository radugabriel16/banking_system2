package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.users.User;

@Getter
@Setter
public final class DeleteCard implements Transactions {
    private String cardNumber;
    private Account account;
    private int timeStamp;

    public DeleteCard(final String cardNumber, final Account account, final int timeStamp) {
        this.cardNumber = cardNumber;
        this.account = account;
        this.timeStamp = timeStamp;
    }

    @Override
    public void execute() {
        account.getCards().remove(account.findCard(cardNumber));
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "The card has been destroyed");
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
