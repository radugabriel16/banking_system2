package org.poo.bank;

public interface Visitor {

    /**
     * Visit the bank and make the desired changes
     */

    void visit(Bank bank, String iban, double amount);
}
