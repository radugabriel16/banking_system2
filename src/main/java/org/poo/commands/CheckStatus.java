package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardStatus;
import org.poo.transactions.ControlTransactions;

@Getter
@Setter
public final class CheckStatus implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public CheckStatus(final CommandInput input, final Bank bank, final Converter convert,
                       final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
        this.control = control;
    }

    @Override
    public void execute() {
        String cardNumber = input.getCardNumber();
        int timeStamp = input.getTimestamp();
        Account account = bank.findParentAccount(cardNumber);
        if (account == null) {
            convert.statusCardError(timeStamp);
        } else if (account.getBalance() <= account.getMinBalance()) {
            CardStatus status = new CardStatus(timeStamp, account);
            control.edit(status, bank.findOwnerCard(cardNumber));
        }
    }
}
