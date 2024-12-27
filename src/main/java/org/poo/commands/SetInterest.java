package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;

@Getter
@Setter
public final class SetInterest implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public SetInterest(final CommandInput input, final Bank bank, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();
        if (bank.findAccount(iban).getType().equals("savings")) {
            SavingsAccount account = (SavingsAccount) bank.findAccount(iban);
            double interest = account.getInterestRate() * account.getBalance();
            account.setBalance(account.getBalance() + interest);
        } else {
            convert.savingAccountError(timeStamp, 0);
        }
    }
}
