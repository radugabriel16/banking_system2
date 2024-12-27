package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.CreateAccount;
import org.poo.users.User;

@Getter
@Setter
public final class AddAccount implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public AddAccount(final CommandInput input, final Bank bank, final Converter converter,
                      final ControlTransactions transactions) {
        this.input = input;
        this.bank = bank;
        this.convert = converter;
        this.control = transactions;
    }

    @Override
    public void execute() {
        String email = input.getEmail();
        String currency = input.getCurrency();
        String type = input.getAccountType();
        double interestRate = input.getInterestRate();
        User user = bank.findUser(email);
        int timeStamp = input.getTimestamp();

        CreateAccount createAccount = new CreateAccount(user, 0, currency, type,
                interestRate, timeStamp);
        control.edit(createAccount, user);
    }
}
