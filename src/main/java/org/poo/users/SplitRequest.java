package org.poo.users;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;

import java.util.ArrayList;
import java.util.Arrays;
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

    public SplitRequest(ArrayList<User> users, List<String> accounts, List<Double> amountList,
                        double amount, String type, int timeStamp, String currency) {
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

    public void decide(User user, String response) {
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
