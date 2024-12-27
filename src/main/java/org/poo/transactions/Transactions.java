package org.poo.transactions;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.users.User;

public interface Transactions {

    /**
     * Every transaction executes a specific action
     */

    void execute();

    /**
     * Every transaction returns an object node to be added in an array in the converter class
     * (there are different aspects to print for every one of them)
     */

    ObjectNode convertJson(User user);

    /**
     * @return the moment of time the transaction was placed
     */

    int getTimestamp();

    /**
     * @return if a transaction is `CardPayment`. It`s used when printing the spending report
     */

    boolean spendingTransaction();

    /**
     * @return the iban associated with the account which was involved in the transaction
     */

    String getIBAN();
}
