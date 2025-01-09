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

    public UpgradePlan(CommandInput input, Bank bank, ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.control = control;
    }

    @Override
    public void execute() {
        String planType = input.getNewPlanType();
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();

        ServiceUpdate update = new ServiceUpdate(timeStamp, planType, iban, bank);
        control.edit(update, bank.findUser(iban));
    }
}
