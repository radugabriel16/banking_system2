package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.CreateCard;

@Getter
@Setter
public final class BuildCard implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;

    public BuildCard(final CommandInput input, final Bank bank,
                     final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.control = control;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        String email = input.getEmail();
        Account account = bank.findAccount(email, iban);
        int timeStamp = input.getTimestamp();
        if (account != null) {
            if (input.getCommand().equals("createCard")) {
                CreateCard createCard = new CreateCard(account, "classic", timeStamp,
                        false, bank, null, control);
                control.edit(createCard, bank.findUser(email));
            } else {
                CreateCard createCard = new CreateCard(account, "oneTime", timeStamp,
                        false, bank, null, control);
                control.edit(createCard, bank.findUser(email));
            }
        }
    }
}
