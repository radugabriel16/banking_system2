package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.users.SplitRequest;
import org.poo.users.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public final class SplitMoney implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;

    public SplitMoney(final CommandInput input, final Bank bank,
                      final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.control = control;
    }

    @Override
    public void execute() {
        String splitType = input.getSplitPaymentType();
        List<String> accounts = input.getAccounts();
        int timeStamp = input.getTimestamp();
        String currency = input.getCurrency();
        double amount = input.getAmount();
        ArrayList<User> users = new ArrayList<>();
        for (String account : accounts) {
            users.add(bank.findUser(account));
        }

        SplitRequest request;
        if (splitType.equals("custom")) {
            request = new SplitRequest(users, accounts, input.getAmountForUsers(), amount, "custom",
                    timeStamp, currency);
        } else {
            request = new SplitRequest(users, accounts, null, amount, "equal",
                    timeStamp, currency);
        }

        for (User user : users) {
            user.getRequests().add(request);
        }
    }
}
