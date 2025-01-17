package org.poo.users;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SplitRequest {
    private ArrayList<User> involved; // List of every person involved in the split payment
    private ArrayList<User> copy;
    private List<String> accounts;
    private List<Double> amountList;
    private double amount;
    private String type;
    private int accepted;
    private int timeStamp;
    private String currency;

    public SplitRequest(final ArrayList<User> users, final List<String> accounts,
                        final List<Double> amountList, final double amount, final String type,
                        final int timeStamp, final String currency) {
        this.involved = users;
        this.copy = new ArrayList<>();
        this.copy.addAll(users);
        this.accounts = accounts;
        this.amountList = amountList;
        this.amount = amount;
        this.type = type;
        this.timeStamp = timeStamp;
        this.currency = currency;
    }

    /**
     * Every involved user have to decide
     * In case the answer is positive, the user is removed from the array and if it becomes empty
     * it means the request is approved
     */

    public void decide(final User user, final String response) {
        if (response.equals("accepted")) {
            copy.remove(user);
            if (copy.isEmpty()) {
                accepted = 1;
            }
        } else {
            accepted = 2;
        }
    }
}
