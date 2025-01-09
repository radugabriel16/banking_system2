package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class SetAlias implements Command {
    private CommandInput input;
    private Bank bank;

    public SetAlias(final CommandInput input, final Bank bank) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        String alias = input.getAlias();
        String iban = input.getAccount();
        if (bank.findAccount(iban) != null)
            bank.findAccount(iban).setAlias(alias);
    }
}
