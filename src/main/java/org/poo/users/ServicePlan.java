package org.poo.users;

import org.poo.bank.Bank;

public interface ServicePlan {

    /**
     * Calculates the commission a user have to pay, based on it`s plan
     */

    double calculateCommission(double amount, Bank bank, String currency);

    /**
     * @return the plan
     */

    String getPlan();
}
