package org.poo.users;

import org.poo.bank.Bank;

public interface ServicePlan {
    double calculateCommission(double amount, Bank bank, String currency);
    String getPlan();
}
