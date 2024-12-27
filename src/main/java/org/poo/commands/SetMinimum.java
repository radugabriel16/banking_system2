package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class SetMinimum implements Command {
    private CommandInput input;
    private Bank bank;

    public SetMinimum(final CommandInput input, final Bank bank) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        double minBalance = input.getMinBalance();
        String iban = input.getAccount();
        Account account = bank.findAccount(iban);
        account.setMinBalance(minBalance);
    }
}

