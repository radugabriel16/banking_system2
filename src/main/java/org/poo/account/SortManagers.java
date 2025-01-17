package org.poo.account;

import java.util.Comparator;

public class SortManagers implements Comparator<Manager> {

    /**
     * This is used to sort alphabetically a list of managers
     */

    public int compare(final Manager m1, final Manager m2) {
        return m1.getUser().getLastName().compareTo(m2.getUser().getLastName());
    }
}
