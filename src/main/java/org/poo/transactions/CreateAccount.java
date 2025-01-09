package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.AccountFactory;

import org.poo.account.BusinessAccount;
import org.poo.users.User;
import org.poo.utils.Utils;

@Getter
@Setter
public final class CreateAccount implements Transactions {
    private User user;
    private String iban;
    private double balance;
    private String currency;
    private String type;
    private double interestRate;
    private int timeStamp;

    public CreateAccount(final User user, final double balance, final String currency,
                         final String type, final double interestRate, final int timeStamp) {
        this.user = user;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.interestRate = interestRate;
        this.timeStamp = timeStamp;
    }

    @Override
    public void execute() {
        iban = Utils.generateIBAN();
        Account account;
        if (type.equals("classic")) {
            account = AccountFactory.createAccount(AccountFactory.AccountType.classic, balance,
                    currency, iban);
            user.getAccounts().add(account);
        } else if (type.equals("savings")) {
            account = AccountFactory.createAccount(AccountFactory.AccountType.savings, balance,
                    currency, iban, interestRate);
            user.getAccounts().add(account);
        } else if (type.equals("business")) {
            account = AccountFactory.createAccount(AccountFactory.AccountType.business, balance,
                    currency, iban);
            user.getAccounts().add(account);
            BusinessAccount newAccount = (BusinessAccount) account;
            newAccount.setOwner(user);
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        node.put("description", "New account created");
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
