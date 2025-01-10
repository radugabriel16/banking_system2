package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.AddInterestRate;
import org.poo.transactions.ControlTransactions;

@Getter
@Setter
public final class SetInterest implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public SetInterest(final CommandInput input, final Bank bank, final Converter convert, final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
        this.control = control;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();

        if (bank.findAccount(iban).getType().equals("savings")) {
            SavingsAccount account = (SavingsAccount) bank.findAccount(iban);
            AddInterestRate addInterest = new AddInterestRate(account, timeStamp);
            control.edit(addInterest, bank.findUser(iban));
        } else {
            convert.savingAccountError(timeStamp, 0);
        }
    }
}
