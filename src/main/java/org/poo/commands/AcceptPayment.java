package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.SplitCustom;
import org.poo.transactions.SplitPayment;
import org.poo.users.SplitRequest;
import org.poo.users.User;

import java.util.List;

@Getter
@Setter
public final class AcceptPayment implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;
    private Converter convert;

    public AcceptPayment(final CommandInput input, final Bank bank,
                         final ControlTransactions control, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.control = control;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String email = input.getEmail();
        User user = bank.findUser(email);

        if (bank.findAccount(email) != null) {
            convert.splitPaymentError(input.getTimestamp(), 1);
            return;
        }

        if (!user.getRequests().isEmpty()) {
            user.getRequests().getFirst().decide(user, "accepted");
            int status = user.getRequests().getFirst().getAccepted();
            if (status != 0) {
                SplitRequest request = user.getRequests().getFirst();
                int timeStamp = request.getTimeStamp();
                String type = request.getType();
                String currency = request.getCurrency();
                double amount = request.getAmount();
                List<String> accounts = request.getAccounts();

                if (type.equals("equal")) {
                    SplitPayment splitPayment = new SplitPayment(timeStamp, currency, amount, bank,
                            accounts, status);
                    control.multipleEdit(splitPayment, request.getInvolved(), accounts, 1,
                            timeStamp);
                } else {
                    SplitCustom splitCustom = new SplitCustom(timeStamp, currency, amount,
                            request.getAmountList(), bank, accounts, status);
                    control.multipleEdit(splitCustom, request.getInvolved(), accounts, 2,
                            timeStamp);
                }
            }
            user.getRequests().removeFirst();
        }
    }
}
