package org.poo.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.SavingsAccount;
import org.poo.users.User;

@Getter
@Setter
public class AddInterestRate implements Transactions {
    private SavingsAccount account;
    private int timeStamp;
    private double bonusMoney;

    public AddInterestRate(SavingsAccount account, int timeStamp) {
        this.account = account;
        this.timeStamp = timeStamp;
    }

    @Override
    public void execute() {
        bonusMoney = account.getInterestRate() * account.getBalance();
        account.setBalance(account.getBalance() + bonusMoney);
    }

    @Override
    public ObjectNode convertJson(User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "Interest rate income");
        node.put("amount", bonusMoney);
        node.put("currency", account.getCurrency());
        return node;
    }

    @Override
    public boolean spendingTransaction() {
        return false;
    }

    @Override
    public String getIBAN() {
        return account.getIban();
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }
}
