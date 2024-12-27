package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.bank.Visitor;
import org.poo.fileio.CommandInput;

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
        double amount = input.getAmount();
        Visitor addFunds = new org.poo.bank.AddFunds();
        bank.accept(addFunds, iban, amount);
    }
}
