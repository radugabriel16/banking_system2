package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.ServiceUpdate;

@Getter
@Setter
public class UpgradePlan implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;
    private Converter convert;

    public UpgradePlan(CommandInput input, Bank bank, ControlTransactions control, Converter convert) {
        this.input = input;
        this.bank = bank;
        this.control = control;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String planType = input.getNewPlanType();
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();

        if (bank.findAccount(iban) == null) {
            convert.notFoundAccount(timeStamp);
        }

        ServiceUpdate update = new ServiceUpdate(timeStamp, planType, iban, bank);
        control.edit(update, bank.findUser(iban));
    }
}
