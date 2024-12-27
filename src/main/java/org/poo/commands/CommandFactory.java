package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;

public class CommandFactory {

    /**
     * It`s used to create a command based on input
     * @return the desired command
     */

    public static Command createCommand(final CommandInput input, final Bank bank,
                                        final Converter converter,
                                        final ControlTransactions control) {
        switch (input.getCommand()) {
            case "addAccount": return new AddAccount(input, bank, converter, control);
            case "addFunds": return new AddFunds(input, bank);
            case "createCard", "createOneTimeCard": return new BuildCard(input, bank, control);
            case "changeInterestRate": return new ChangeInterest(input, bank, converter, control);
            case "checkCardStatus": return new CheckStatus(input, bank, converter, control);
            case "deleteAccount": return new DestroyAccount(input, bank, converter, control);
            case "deleteCard": return new DestroyCard(input, bank, control);
            case "payOnline": return  new PayOnline(input, bank, converter, control);
            case "printTransactions": return new PrintTransactions(input, bank, converter);
            case "printUsers": return new PrintUsers(input, bank, converter);
            case "report": return new Report(input, bank, converter);
            case "sendMoney": return new SendMoney(input, bank, control);
            case "setAlias": return new SetAlias(input, bank);
            case "addInterest": return new SetInterest(input, bank, converter);
            case "setMinimumBalance": return new SetMinimum(input, bank);
            case "spendingsReport": return new SpendingsReport(input, bank, converter);
            case "splitPayment": return new SplitMoney(input, bank, control);
            default: throw new IllegalArgumentException("That command is not supported");
        }
    }
}