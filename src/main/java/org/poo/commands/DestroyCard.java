package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.DeleteCard;

@Getter
@Setter
public final class DestroyCard implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;

    public DestroyCard(final CommandInput input, final Bank bank,
                       final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.control = control;
    }

    @Override
    public void execute() {
        String cardNumber = input.getCardNumber();
        int timeStamp = input.getTimestamp();
        DeleteCard deleteCard = new DeleteCard(cardNumber, bank.findParentAccount(cardNumber),
                timeStamp);
        control.edit(deleteCard, bank.findOwnerCard(cardNumber));
    }
}
