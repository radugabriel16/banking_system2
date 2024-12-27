package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.users.User;

@Getter
@Setter
public final class CardStatus implements Transactions {
    private int timeStamp;
    private Account account;

    public CardStatus(final int timeStamp, final Account account) {
        this.timeStamp = timeStamp;
        this.account = account;
    }

    @Override
    public void execute() {
        account.blockCards();
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "You have reached the minimum amount of funds, the card will be "
                + "frozen");
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
