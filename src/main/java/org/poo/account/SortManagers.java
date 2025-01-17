package org.poo.account;

import org.poo.commerciants.Commerciant;

import java.util.Comparator;

public class SortManagers implements Comparator<Manager> {
    public int compare(final Manager m1, final Manager m2) {
        return m1.getUser().getLastName().compareTo(m2.getUser().getLastName());
    }
}
