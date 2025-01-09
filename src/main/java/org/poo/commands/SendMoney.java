package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.TransferMoney;
import org.poo.users.User;

@Getter
@Setter
public final class SendMoney implements Command {
    private CommandInput input;
    private Bank bank;
    private ControlTransactions control;
    private Converter convert;

    public SendMoney(final CommandInput input, final Bank bank,
                     final ControlTransactions control, final Converter convert) {
        this.input = input;
        this.bank = bank;
        this.control = control;
        this.convert = convert;
    }

    @Override
    public void execute() {
        String sender = input.getAccount();
        String receiver = input.getReceiver();
        double amount = input.getAmount();
        String description = input.getDescription();
        int timeStamp = input.getTimestamp();

        User senderUser = bank.findUser(sender);
        User receiverUser = bank.findUser(receiver);

        if (bank.findUser(receiver) == null || bank.findUser(sender) == null) {
            convert.sendMoneyError(timeStamp);
        }

        else if (bank.findAccount(sender) != null && !bank.searchedAfterAlias(sender)
                && bank.findAccount(receiver) != null) {
            TransferMoney transfer = new TransferMoney(bank.findAccount(sender),
                    bank.findAccount(receiver), amount, description, bank, timeStamp,
                    receiver);
            transfer.setCurrentIban(sender);
            transfer.setTransferType("sent");

            control.edit(transfer, senderUser);
            if (transfer.isSuccess()) {
                TransferMoney transferCopy = new TransferMoney(transfer);
                transferCopy.setTransferType("received");
                if (sender.equals(receiver)) {
                    senderUser.getHistory().add(transferCopy);
                    transferCopy.setCurrentIban(sender);
                } else {
                    String iban = null;
                    if (!bank.searchedAfterAlias(receiver)) {
                        iban = receiver;
                    } else {
                        iban = receiverUser.findIban(receiver);
                    }
                    transferCopy.setCurrentIban(iban);
                    receiverUser.getHistory().add(transferCopy);
                }
            }
        }
    }
}
