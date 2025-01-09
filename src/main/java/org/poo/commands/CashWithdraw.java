package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.ExtractCash;

@Getter
@Setter
public class CashWithdraw implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;
    private Converter convert;

    public CashWithdraw(CommandInput input, Bank bank, ControlTransactions control, Converter convert) {
        this.input = input;
        this.bank = bank;
        this.control = control;
        this.convert = convert;
    }

    @Override
    public void execute() {
        int timeStamp = input.getTimestamp();
        String cardNumber = input.getCardNumber();
        double amount = input.getAmount();
        String email = input.getEmail();
        String location = input.getLocation();

        if (bank.findCard(cardNumber) == null) {
            convert.cashWithdrawalError(timeStamp, 1);
        } else if (bank.findUser(email) == null) {
            convert.cashWithdrawalError(timeStamp, 2);
        } else {
            ExtractCash extractCash = new ExtractCash(timeStamp, email, cardNumber, amount, location, bank);
            control.edit(extractCash, bank.findOwnerCard(cardNumber));
        }
    }
}
