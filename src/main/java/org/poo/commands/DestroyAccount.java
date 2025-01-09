package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.ErrorDelete;
import org.poo.users.DeleteAccount;

@Getter
@Setter
public final class DestroyAccount implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public DestroyAccount(final CommandInput input, final Bank bank, final Converter convert,
                          final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
        this.control = control;
    }

    @Override
    public void execute() {
        String email = input.getEmail();
        String iban = input.getAccount();
        DeleteAccount delete = new DeleteAccount();
        int timeStamp = input.getTimestamp();
        if (bank.findAccount(email, iban) != null) {
            if (bank.findAccount(email, iban).getBalance() == 0) {
                bank.findUser(email).accept(delete, bank.findAccount(email, iban));
                convert.deleteAccount(timeStamp);
            } else {
                convert.deleteAccountFailed(timeStamp);
                ErrorDelete deleteTransaction = new ErrorDelete(timeStamp, iban);
                control.edit(deleteTransaction, bank.findUser(email));
            }
        }
    }
}
