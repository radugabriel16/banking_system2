package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

@Getter
@Setter
public final class ErrorDelete implements Transactions {
    private int timeStamp;
    private String iban;

    public ErrorDelete(final int timeStamp, final String iban) {
        this.timeStamp = timeStamp;
        this.iban = iban;
    }

    @Override
    public void execute() { }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "Account couldn't be deleted - there are funds remaining");
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
        return iban;
    }
}
