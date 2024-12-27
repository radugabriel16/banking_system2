package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.SavingsAccount;
import org.poo.users.User;

@Getter
@Setter
public final class ChangeInterestRate implements Transactions {
    private SavingsAccount account;
    private double rate;
    private int timeStamp;

    public ChangeInterestRate(final SavingsAccount account, final double rate,
                              final int timeStamp) {
        this.account = account;
        this.rate = rate;
        this.timeStamp = timeStamp;
    }


    @Override
    public void execute() {
        account.setInterestRate(rate);
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "Interest rate of the account changed to " + rate);
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
