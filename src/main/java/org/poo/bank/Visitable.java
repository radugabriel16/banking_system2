package org.poo.bank;

public interface Visitable {

    /**
     *  Accepts the `AddFunds` visitor
     */

    void accept(Visitor visitor, String iban, double amount);
}
