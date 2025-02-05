package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.BusinessAccount;
import org.poo.bank.Bank;
import org.poo.card.Card;
import org.poo.converter.Converter;
import org.poo.fileio.CommandInput;
import org.poo.transactions.CardPayment;
import org.poo.transactions.ControlTransactions;
import org.poo.transactions.CreateCard;
import org.poo.users.User;

@Getter
@Setter
public final class PayOnline implements Command {
    private CommandInput input;
    private Bank bank;
    private Converter convert;
    private ControlTransactions control;

    public PayOnline(final CommandInput input, final Bank bank, final Converter convert,
                      final ControlTransactions control) {
        this.input = input;
        this.bank = bank;
        this.convert = convert;
        this.control = control;
    }

    @Override
    public void execute() {
        String cardNumber = input.getCardNumber();
        double amount = input.getAmount();
        String currency = input.getCurrency();
        String description = input.getDescription();
        String commerciant = input.getCommerciant();
        String email = input.getEmail();
        int timeStamp = input.getTimestamp();

        boolean ok = false;
        for (User user : bank.getBankUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getType().equals("business")) {
                    BusinessAccount businessAccount = (BusinessAccount) account;
                    if ((businessAccount.isUserInvolved(bank.findUser(email))
                        && !businessAccount.isUserInvolved(bank.findOwnerCard(cardNumber)))
                        || (!businessAccount.isUserInvolved(bank.findUser(email))
                            && businessAccount.isUserInvolved(bank.findOwnerCard(cardNumber)))) {
                        convert.cardPayment(timeStamp);
                        return;
                    } else if (businessAccount.isUserInvolved(bank.findUser(email))
                                && businessAccount.isUserInvolved(bank.findOwnerCard(cardNumber))) {
                        ok = true;
                    }
                }
            }
        }

        if (bank.findOwnerCard(cardNumber) == null && !ok) {
            convert.cardPayment(timeStamp);
        } else if (amount > 0) {
            Card card = bank.findCard(cardNumber);
            Account account = bank.findParentAccount(cardNumber);
            CardPayment payment = new CardPayment(cardNumber, amount, currency,
                    description, commerciant, bank, bank.findUser(email), timeStamp,
                    card.getStatus(), account.getIban());
            control.edit(payment, bank.findUser(email));
            if (payment.isOneTimeCard()) {
                CreateCard createCard = new CreateCard(account, "oneTime", timeStamp,
                        true, bank, card, control);
                control.edit(createCard, bank.findOwnerCard(cardNumber));
            }
        }
    }
}
