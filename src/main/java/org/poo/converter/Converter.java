package org.poo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.Associate;
import org.poo.account.BusinessAccount;
import org.poo.card.Card;
import org.poo.commerciants.Commerciant;
import org.poo.transactions.CardPayment;
import org.poo.transactions.Transactions;
import org.poo.users.User;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public final class Converter {
    private ArrayNode output;

    public Converter(final ArrayNode output) {
        this.output = output;
    }

    /**
     * It converts the users with their accounts and cards
     */

    public void printUsers(final ArrayList<User> users, final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "printUsers");

        ArrayNode people = mapper.createArrayNode();

        for (User user : users) {
            ObjectNode specificUser = mapper.createObjectNode();
            specificUser.put("firstName", user.getFirstName());
            specificUser.put("lastName", user.getLastName());
            specificUser.put("email", user.getEmail());

            ArrayNode accountsArray = mapper.createArrayNode();
            ArrayList<Account> accounts = user.getAccounts();
            for (Account account : accounts) {
                ObjectNode specificAccount = mapper.createObjectNode();
                specificAccount.put("IBAN", account.getIban());
                specificAccount.put("balance", account.getBalance());
                specificAccount.put("currency", account.getCurrency());
                specificAccount.put("type", account.getType());

                ArrayNode cardsArray = mapper.createArrayNode();
                ArrayList<Card> cards = account.getCards();
                for (Card card : cards) {
                    ObjectNode specificCard = mapper.createObjectNode();
                    specificCard.put("cardNumber", card.getCardNumber());
                    specificCard.put("status", card.getStatus());
                    cardsArray.add(specificCard);
                }
                specificAccount.set("cards", cardsArray);
                accountsArray.add(specificAccount);
            }
            specificUser.set("accounts", accountsArray);
            people.add(specificUser);
        }
        text.set("output", people);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the successful delete message of an account
     */

    public void deleteAccount(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "deleteAccount");

        ObjectNode message = mapper.createObjectNode();
        message.put("success", "Account deleted");
        message.put("timestamp", timeStamp);

        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the failed delete message of an account
     */

    public void deleteAccountFailed(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "deleteAccount");
        ObjectNode message = mapper.createObjectNode();
        message.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        message.put("timestamp", timeStamp);
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the error message of not founding a card
     */

    public void cardPayment(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "payOnline");

        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "Card not found");

        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the whole list of transactions for a specific user
     */

    public void printTransactions(final ArrayList<Transactions> transactions, final int timeStamp,
                                  final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "printTransactions");

        ArrayNode operations = mapper.createArrayNode();
        for (Transactions transaction : transactions) {
            ObjectNode specificTransaction = transaction.convertJson(user);
            operations.add(specificTransaction);
        }
        text.set("output", operations);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the error message of not founding a card
     */

    public void statusCardError(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "checkCardStatus");

        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "Card not found");

        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts all transactions made by an account in a specific interval of time
     */

    public void presentReport(final int timeStamp, final Account account, final User user,
                              final int start, final int end) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "report");

        ObjectNode message = mapper.createObjectNode();

        message.put("IBAN", account.getIban());
        message.put("balance", account.getBalance());
        message.put("currency", account.getCurrency());

        ArrayNode transactions = mapper.createArrayNode();
        for (int i = 0; i < user.getHistory().size(); i++) {
            if (user.getHistory().get(i).getTimestamp() >= start
                    && user.getHistory().get(i).getTimestamp() <= end
                && user.getHistory().get(i).getIBAN().equals(account.getIban())) {
                ObjectNode transaction = user.getHistory().get(i).convertJson(user);
                transactions.add(transaction);
            }
        }

        message.set("transactions", transactions);
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts all successful payments made by an account in a specific interval of time
     */

    public void spendingsReport(final int timeStamp, final Account account, final User user,
                                final int start, final int end,
                                final HashMap<String, Double> comm) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "spendingsReport");
        ObjectNode message = mapper.createObjectNode();

        if (account.getType().equals("savings")) {
            message.put("error", "This kind of report is not supported for a saving account");
            text.set("output", message);
        } else {
            message.put("IBAN", account.getIban());
            message.put("balance", account.getBalance());
            message.put("currency", account.getCurrency());

            ArrayNode transactions = mapper.createArrayNode();
            for (int i = 0; i < user.getHistory().size(); i++) {
                Transactions transaction = user.getHistory().get(i);
                if (transaction.spendingTransaction()) {
                    CardPayment payment = (CardPayment) transaction;
                    String iban = payment.getIBAN();
                    if (transaction.getTimestamp() >= start && transaction.getTimestamp() <= end
                            && account.getIban().equals(iban) && payment.isSuccess()) {
                        ObjectNode operation = user.getHistory().get(i).convertJson(user);
                        transactions.add(operation);
                    }
                }
            }
            message.set("transactions", transactions);

            ArrayNode commerciants = mapper.createArrayNode();
            for (String key : comm.keySet()) {
                ObjectNode commerciant = mapper.createObjectNode();
                commerciant.put("commerciant", key);
                commerciant.put("total", comm.get(key));
                commerciants.add(commerciant);
            }
            message.set("commerciants", commerciants);
        }
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the error message of not founding an account
     */

    public void accountErrorReport(final int timeStamp, final int errorType) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        if (errorType == 0) {
            text.put("command", "report");
        } else {
            text.put("command", "spendingsReport");
        }

        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "Account not found");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    /**
     * It converts the error message of not choosing a savings account
     */

    public void savingAccountError(final int timeStamp, final int errorType) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        if (errorType == 0) {
            text.put("command", "addInterest");
        } else {
            text.put("command", "changeInterestRate");
        }

        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "This is not a savings account");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void cashWithdrawalError(final int timeStamp, final int errorType) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "cashWithdrawal");
        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        if (errorType == 1)
            message.put("description", "Card not found");
        else
            message.put("description", "User not found");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void sendMoneyError(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "sendMoney");
        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "User not found");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void notOwnerError(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "changeSpendingLimit");
        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "You must be owner in order to change spending limit.");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void alreadyAssociateError(final int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "addNewBusinessAssociate");
        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "The user is already an associate of the account.");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void transactionsBusiness(int timeStamp, int start, int end, BusinessAccount account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "businessReport");

        ObjectNode message = mapper.createObjectNode();
        message.put("IBAN", account.getIban());
        message.put("balance", account.getBalance());
        message.put("currency", account.getCurrency());
        message.put("spending limit", account.getSpendingLimit());
        message.put("deposit limit", account.getDepositLimit());
        message.put("statistics type", "transaction");

        ArrayNode managers = mapper.createArrayNode();
        for (Associate associate : account.getAssociates()) {
            if (associate.getType().equals("manager")) {
                ObjectNode manager = mapper.createObjectNode();
                manager.put("username", associate.getUser().getLastName() + " " + associate.getUser().getFirstName());
                manager.put("spent", associate.getSpent(start, end));
                manager.put("deposited", associate.getDeposited(start, end));
                managers.add(manager);
            }
        }

        message.set("managers", managers);

        ArrayNode employees = mapper.createArrayNode();
        for (Associate associate : account.getAssociates()) {
            if (associate.getType().equals("employee")) {
                ObjectNode employee = mapper.createObjectNode();
                employee.put("username", associate.getUser().getLastName() + " " + associate.getUser().getFirstName());
                employee.put("spent", associate.getSpent(start, end));
                employee.put("deposited", associate.getDeposited(start, end));
                employees.add(employee);
            }
        }

        message.set("employees", employees);
        message.put("total spent", account.getTotalSpent());
        message.put("total deposited", account.getTotalDeposit());
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void notFoundAccount(int timeStamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        text.put("command", "upgradePlan");
        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "Account not found");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }

    public void splitPaymentError(final int timeStamp, int type) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode text = mapper.createObjectNode();
        if (type == 1)
            text.put("command", "acceptSplitPayment");
        else
            text.put("command", "rejectSplitPayment");

        ObjectNode message = mapper.createObjectNode();
        message.put("timestamp", timeStamp);
        message.put("description", "User not found");
        text.set("output", message);
        text.put("timestamp", timeStamp);
        output.add(text);
    }
}
