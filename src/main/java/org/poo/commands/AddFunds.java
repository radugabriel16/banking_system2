package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.bank.Visitor;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@Getter
@Setter
public final class AddFunds implements Command {
    private CommandInput input;
    private Bank bank;

    public AddFunds(final CommandInput input, final Bank bank) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        String email = input.getEmail();
        double amount = input.getAmount();
        int timeStamp = input.getTimestamp();

        User moneyDeliver = bank.findUser(email);
        Account account = bank.findAccount(iban);

        Visitor addFunds = new org.poo.bank.AddFunds();
        bank.accept(addFunds, account, amount, moneyDeliver, timeStamp);
    }
}
