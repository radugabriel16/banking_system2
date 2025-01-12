package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Associate;
import org.poo.account.AssociateFactory;
import org.poo.account.BusinessAccount;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

@Getter
@Setter
public class AddNewBusinessAssociate implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public AddNewBusinessAssociate(CommandInput input, Bank bank, Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String iban = input.getAccount();
        String role = input.getRole();
        String email = input.getEmail();
        int timeStamp = input.getTimestamp();

        BusinessAccount account = (BusinessAccount) bank.findAccount(iban);
        User user = bank.findUser(email);

        for (Associate associate : account.getAssociates()) {
            if (associate.getUser().equals(user)) {
                convert.alreadyAssociateError(timeStamp);
                return;
            }
        }

        Associate associate;
        if (role.equals("manager")) {
            associate = AssociateFactory.createAssociate(AssociateFactory.AssociateType.manager, user);
            account.getAssociates().add(associate);
        } else if (role.equals("employee")) {
            associate = AssociateFactory.createAssociate(AssociateFactory.AssociateType.employee, user);
            account.getAssociates().add(associate);
        }
    }
}
