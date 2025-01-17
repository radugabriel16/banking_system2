package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;

import java.io.Serializable;

@Getter
@Setter
public class BusinessReport implements Command {
    private Converter convert;
    private CommandInput input;
    private Bank bank;

    public BusinessReport(Converter convert, CommandInput input, Bank bank) {
        this.convert = convert;
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        String type = input.getType();
        int start = input.getStartTimestamp();
        int end = input.getEndTimestamp();
        String iban = input.getAccount();
        int timeStamp = input.getTimestamp();

        BusinessAccount account = (BusinessAccount) bank.findAccount(iban);
        if (type.equals("transaction")) {
            convert.transactionsBusiness(timeStamp, start, end, account);
        } else {
            convert.commerciantBusiness(timeStamp, start, end, account);
        }
    }
}
