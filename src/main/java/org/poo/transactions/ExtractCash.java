package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.users.User;

@Getter
@Setter
public class ExtractCash implements Transactions {
    private int timeStamp;
    private String email;
    private String cardNumber;
    private double amount;
    private String location;
    private int messageType;
    private Bank bank;

    public ExtractCash(int timeStamp, String email, String cardNumber, double amount, String location,
                        Bank bank) {
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
            messageType = 1;
            return;
        }
        Account account = bank.findParentAccount(cardNumber);
        double newAmount = bank.getMoneyConversion().convertMoney("RON", account.getCurrency(), amount);
        newAmount += user.getServicePlan().calculateCommission(newAmount, bank, account.getCurrency());

        if (account.getBalance() < newAmount) {
            messageType = 2;
            return;
        }
        else if (account.getBalance() - newAmount <= account.getMinBalance()) {
            messageType = 3;
            return;
        }
        account.setBalance(account.getBalance() - newAmount);
    }

    @Override
    public ObjectNode convertJson(User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (messageType == 0) {
            node.put("description", "Cash withdrawal of " + amount);
            node.put("amount", amount);
        } else if (messageType == 1) {
            node.put("description", "The card is frozen");
        } else if (messageType == 2) {
            node.put("description", "Insufficient funds");
        } else if (messageType == 3) {
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
