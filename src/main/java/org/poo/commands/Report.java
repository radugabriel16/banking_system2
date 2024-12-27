package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class Report implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public Report(final CommandInput input, final Bank bank, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        int start = input.getStartTimestamp();
        int end = input.getEndTimestamp();
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();
        if (bank.findAccount(iban) != null) {
            convert.presentReport(timeStamp, bank.findAccount(iban), bank.findUser(iban), start,
                    end);
        } else {
            convert.accountErrorReport(timeStamp, 0);
        }
    }
}
