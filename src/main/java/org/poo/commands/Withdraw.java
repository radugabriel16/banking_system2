package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.ExtractMoney;

@Getter
@Setter
public class Withdraw implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;
    private int typeMessage;

    public Withdraw(final CommandInput input, final Bank bank,
                     final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.control = control;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        double amount = input.getAmount();
        String currency = input.getCurrency();
        int timeStamp = input.getTimestamp();

        ExtractMoney extract = new ExtractMoney(timeStamp, currency, amount, iban, bank);
        control.edit(extract, bank.findUser(iban));
        if (extract.getDoubleMessage() == 1)
            bank.findUser(iban).getHistory().add(extract);
    }
}
