package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.Associate;
import org.poo.account.BusinessAccount;
import org.poo.bank.Bank;
import org.poo.users.User;

@Getter
@Setter
public final class TransferMoney implements Transactions {
    private Account accountSender;
    private Account accountReceiver;
    private double amount;
    private String description;
    private Bank bank;
    private int timeStamp;
    private boolean success = false;
    // It can be both iban or alias
    private String receiverType;
    private String currentIban;
    private String transferType;

    public TransferMoney(final Account sender, final Account receiver, final double amount,
                         final String description, final Bank bank, final int timeStamp,
                         final String receiverType) {
        this.accountSender = sender;
        this.accountReceiver = receiver;
        this.amount = amount;
        this.description = description;
        this.bank = bank;
        this.timeStamp = timeStamp;
        this.receiverType = receiverType;
    }

    public TransferMoney(final TransferMoney original) {
        this.accountSender = original.accountSender;
        this.accountReceiver = original.accountReceiver;
        this.amount = original.amount;
        this.description = original.description;
        this.bank = original.bank;
        this.timeStamp = original.timeStamp;
        this.receiverType = original.receiverType;
        this.success = original.success;
        this.currentIban = original.currentIban;
        this.transferType = original.transferType;
    }

    @Override
    public void execute() {
        String currencySender = accountSender.getCurrency();
        String currencyReceiver = accountReceiver.getCurrency();

        User user = bank.findUser(accountSender.getIban());
        double newAmount = amount;
        newAmount += user.getServicePlan().calculateCommission(amount, bank, accountSender.getCurrency());

        if (accountSender.getBalance() >= newAmount) {
            if (accountSender.getType().equals("business")) {
                BusinessAccount businessAccount = (BusinessAccount)accountSender;
                Associate associate = businessAccount.getAssociate(user);
                if (associate != null) {
                    int result = associate.pay(amount, businessAccount, timeStamp);
                    if (result == 0) {
                        return;
                    }
                }
            }
            accountSender.setBalance(accountSender.getBalance() - newAmount);
            double receiverAmount = bank.getMoneyConversion().convertMoney(currencySender,
                    currencyReceiver, amount);
            accountReceiver.setBalance(accountReceiver.getBalance() + receiverAmount);
            success = true;
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (!success) {
            node.put("description", "Insufficient funds");
        } else {
            String currency = null;
            double amountPerspective = 0;
            if (transferType.equals("sent")) {
                currency = accountSender.getCurrency();
                amountPerspective = amount;
            } else if (transferType.equals("received")) {
                currency = accountReceiver.getCurrency();
                amountPerspective = bank.getMoneyConversion().convertMoney(accountSender
                                .getCurrency(), accountReceiver.getCurrency(), amount);
            }
            node.put("description", description);
            node.put("senderIBAN", accountSender.getIban());

            // Verifies if the receiver string is iban or alias to know what to print
            if (bank.searchedAfterAlias(receiverType)) {
                node.put("receiverIBAN", accountReceiver.getAlias());
            } else {
                node.put("receiverIBAN", accountReceiver.getIban());
            }
            node.put("amount", amountPerspective + " " + currency);
            node.put("transferType", transferType);
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
        return currentIban;
    }
}
