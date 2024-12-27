package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.SplitPayment;
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
        List<String> accounts = input.getAccounts();
        int timeStamp = input.getTimestamp();
        String currency = input.getCurrency();
        double amount = input.getAmount();
        ArrayList<User> users = new ArrayList<>();
        for (String account : accounts) {
            users.add(bank.findUser(account));
        }
        SplitPayment splitPayment = new SplitPayment(timeStamp, currency, amount, bank, accounts);
        control.multipleEdit(splitPayment, users, accounts);
    }
}
