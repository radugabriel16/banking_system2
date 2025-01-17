package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.exchange_rate.MoneyConversion;
import org.poo.users.ServicePlan;
import org.poo.users.User;

@Getter
@Setter
public final class ExtractCash implements Transactions {
    private int timeStamp;
    private String email;
    private String cardNumber;
    private double amount;
    private String location;
    private int messageType;
    private Bank bank;
    private static final int MESSAGE1 = 1;
    private static final int MESSAGE2 = 2;
    private static final int MESSAGE3 = 3;

    public ExtractCash(final int timeStamp, final String email, final String cardNumber,
                       final double amount, final String location, final Bank bank) {
        this.timeStamp = timeStamp;
        this.email = email;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.location = location;
        this.bank = bank;
    }

    @Override
    public void execute() {
        User user = bank.findUser(email);
        Card card = bank.findCard(cardNumber);
        if (card.getStatus().equals("frozen")) {
            messageType = MESSAGE1;
            return;
        }
        Account account = bank.findParentAccount(cardNumber);
        MoneyConversion conversion = bank.getMoneyConversion();
        double newAmount = conversion.convertMoney("RON", account.getCurrency(), amount);
        ServicePlan plan = user.getServicePlan();
        newAmount += plan.calculateCommission(newAmount, bank, account.getCurrency());

        if (account.getBalance() < newAmount) {
            messageType = MESSAGE2;
            return;
        }
        else if (account.getBalance() - newAmount <= account.getMinBalance()) {
            messageType = MESSAGE3;
            return;
        }
        account.setBalance(account.getBalance() - newAmount);
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == 0) {
            node.put("description", "Cash withdrawal of " + amount);
            node.put("amount", amount);
        } else if (messageType == MESSAGE1) {
            node.put("description", "The card is frozen");
        } else if (messageType == MESSAGE2) {
            node.put("description", "Insufficient funds");
        } else if (messageType == MESSAGE3) {
            node.put("description", "Cannot perform payment due to a minimum balance being set");
        }
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
        return bank.findParentAccount(cardNumber).getIban();
    }
}
