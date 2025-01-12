package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.Associate;
import org.poo.account.BusinessAccount;
import org.poo.account.Employee;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@Getter
@Setter
public class ChangeSpendingLimit implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public ChangeSpendingLimit(CommandInput input, Bank bank, Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String email = input.getEmail();
        String iban = input.getAccount();
        double amount = input.getAmount();
        int timeStamp = input.getTimestamp();

        User user = bank.findUser(email);
        Account account = bank.findAccount(iban);
        if (account.getType().equals("business")) {
            BusinessAccount businessAccount = (BusinessAccount) bank.findAccount(iban);
            if (businessAccount.getOwner().equals(user)) {
                businessAccount.setSpendingLimit(amount);
            } else {
                convert.notOwnerError(timeStamp);
            }
        }
    }
}
