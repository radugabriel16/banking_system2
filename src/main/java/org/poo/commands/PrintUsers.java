package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.users.User;

import java.util.ArrayList;

@Getter
@Setter
public final class PrintUsers implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;

    public PrintUsers(final CommandInput input, final Bank bank, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
    }

    @Override
    public void execute() {
        int timeStamp = input.getTimestamp();
        ArrayList<User> users = bank.getBankUsers();
        convert.printUsers(users, timeStamp);
    }
}
