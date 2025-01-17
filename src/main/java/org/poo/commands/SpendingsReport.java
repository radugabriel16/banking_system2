package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@Getter
@Setter
public final class SpendingsReport implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public SpendingsReport(final CommandInput input, final Bank bank, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        int timeStamp = input.getTimestamp();
        String iban = input.getAccount();
        int start = input.getStartTimestamp();
        int end = input.getEndTimestamp();
        Account account = bank.findAccount(iban);
        if (account != null) {
            User user = bank.findUser(iban);
            convert.spendingsReport(timeStamp, account, user, start, end, account.getPayments());
        } else {
            convert.accountErrorReport(timeStamp, 1);
        }
    }
}
