package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ChangeInterestRate;
import org.poo.transactions.ControlTransactions;

@Getter
@Setter
public final class ChangeInterest implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public ChangeInterest(final CommandInput input, final Bank bank, final Converter convert,
                          final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
        this.control = control;
    }

    @Override
    public void execute() {
        int timeStamp = input.getTimestamp();
        String iban = input.getAccount();
        double rate = input.getInterestRate();
        if (bank.findAccount(iban).getType().equals("savings")) {
            SavingsAccount account = (SavingsAccount) bank.findAccount(iban);
            ChangeInterestRate change = new ChangeInterestRate(account, rate, timeStamp);
            control.edit(change, bank.findUser(iban));
        } else {
            convert.savingAccountError(timeStamp, 1);
        }
    }
}
