package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import org.poo.account.Associate;
import org.poo.account.BusinessAccount;
import org.poo.account.CommerciantAccount;
import org.poo.bank.Bank;
import org.poo.commerciants.Commerciant;
import org.poo.exchange_rate.MoneyConversion;
import org.poo.users.ServiceFactory;
import org.poo.users.ServicePlan;
import org.poo.users.Silver;
import org.poo.users.User;

@Getter
@Setter
public final class TransferToCommerciant implements Transactions {
    private Account accountSender;
    private CommerciantAccount accountReceiver;
    private double amount;
    private String description;
    private Bank bank;
    private int timeStamp;
    private boolean success = false;

    public TransferToCommerciant(final Account sender, final CommerciantAccount receiver,
                                 final double amount, final String description, final Bank bank,
                                 final int timeStamp) {
        this.accountSender = sender;
        this.accountReceiver = receiver;
        this.amount = amount;
        this.description = description;
        this.bank = bank;
        this.timeStamp = timeStamp;
    }

    @Override
    public void execute() {
        User user = bank.findUser(accountSender.getIban());
        double newAmount = amount;
        ServicePlan plan = user.getServicePlan();
        newAmount += plan.calculateCommission(amount, bank, accountSender.getCurrency());

        if (accountSender.getBalance() >= newAmount) {
            MoneyConversion conversion = bank.getMoneyConversion();
            double amountRon = conversion.convertMoney(accountSender.getCurrency(), "RON", amount);
            if (amountRon > 300 && user.getServicePlan().getPlan().equals("silver")) {
                Silver silverPlan = (Silver) user.getServicePlan();
                silverPlan.setPurchases(silverPlan.getPurchases() + 1);

                // Possible to upgrade to gold
                if (silverPlan.getPurchases() == 5) {
                    ServicePlan newPlan = ServiceFactory.createService(ServiceFactory.ServiceType.Gold);
                    user.setServicePlan(newPlan);
                }
            }
            Commerciant comm = accountReceiver.getCommerciant();
            if (accountSender.getType().equals("business")) {
                BusinessAccount businessAccount = (BusinessAccount) accountSender;
                Associate associate = businessAccount.getAssociate(user);
                if (associate != null) {
                    int result = associate.pay(amount, businessAccount, timeStamp,
                            accountReceiver.getCommerciant());
                    if (result == 0) {
                        return;
                    } else if (!businessAccount.getCommerciants().contains(comm)) {
                        businessAccount.getCommerciants().add(comm);
                    }
                }
            }

            comm.getCashback().pay(amount, comm, user, accountSender, bank);

            accountSender.setBalance(accountSender.getBalance() - newAmount);
            success = true;
        }
    }

    @Override
    public ObjectNode convertJson(final User user) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timeStamp);
        if (!success) {
            node.put("description", "Insufficient funds");
        } else {
            node.put("description", description);
            node.put("senderIBAN", accountSender.getIban());
            node.put("receiverIBAN", accountReceiver.getIban());

            node.put("amount", amount + " " + accountSender.getCurrency());
            node.put("transferType", "sent");
        }
        return node;
    }

    @Override
    public String getIBAN() {
        return accountSender.getIban();
    }

    @Override
    public boolean spendingTransaction() {
        return false;
    }

    @Override
    public int getTimestamp() {
        return timeStamp;
    }
}
