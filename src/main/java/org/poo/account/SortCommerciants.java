package org.poo.account;

import org.poo.commerciants.Commerciant;

import java.util.Comparator;

public final class SortCommerciants implements Comparator<Commerciant> {

    /**
     * This is used to sort alphabetically a list of commerciants
     */

    public int compare(final Commerciant c1, final Commerciant c2) {
        return c1.getName().compareTo(c2.getName());
    }
}
