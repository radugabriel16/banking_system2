package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class PrintTransactions implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public PrintTransactions(final CommandInput input, final Bank bank, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String email = input.getEmail();
        int timestamp = input.getTimestamp();
        convert.printTransactions(bank.findUser(email).getHistory(), timestamp,
                bank.findUser(email));
    }
}
