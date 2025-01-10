package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
import org.poo.users.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class ControlTransactions {

    /**
     * It adds the transaction in the user`s history and execute it
     * @param transaction representing the specific transaction
     * @param user representing the one who made a transaction
     */

    public void edit(final Transactions transaction, final User user) {
        if (user != null) {
            user.getHistory().add(transaction);
            transaction.execute();
        }
    }

    /**
     * In the case of split payment, It`s essential to make copies of the original transaction and
     * put them in every involved user`s history. It just updates the iban to be correctly printed
     * at reports
     */

    public void multipleEdit(final Transactions transaction, final ArrayList<User> user,
                             final List<String> iban, final int type, final int timeStamp) {
        transaction.execute();

        for (int i = 0; i < user.size(); i++) {
            if (user.get(i) != null) {
                int added = 0;
                if (type == 1) {
                    SplitPayment payment = (SplitPayment) transaction;
                    SplitPayment copy = new SplitPayment(payment);
                    copy.setCurrentIban(iban.get(i));

                    // There is possible other transactions are done until the payment is approved or not

                    for (int j = 0; j < user.get(i).getHistory().size(); ++j) {
                        if (user.get(i).getHistory().get(j).getTimestamp() > timeStamp) {
                            user.get(i).getHistory().add(j, copy);
                            added = 1;
                            break;
                        }
                    }
                    if (added == 0)
                        user.get(i).getHistory().add(copy);
                } else {
                    SplitCustom payment = (SplitCustom) transaction;
                    SplitCustom copy = new SplitCustom(payment);
                    copy.setCurrentIban(iban.get(i));
                    for (int j = 0; j < user.get(i).getHistory().size(); ++j) {
                        if (user.get(i).getHistory().get(j).getTimestamp() > timeStamp) {
                            user.get(i).getHistory().add(j, copy);
                            added = 1;
                            break;
                        }
                    }
                    if (added == 0)
                        user.get(i).getHistory().add(copy);
                }
            }
        }
    }
}
