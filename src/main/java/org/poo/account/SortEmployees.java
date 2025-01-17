package org.poo.account;

import java.util.Comparator;

public class SortEmployees implements Comparator<Employee> {
    public int compare(final Employee e1, final Employee e2) {
        return e1.getUser().getLastName().compareTo(e2.getUser().getLastName());
    }
}
