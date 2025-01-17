package org.poo.account;

import org.poo.users.User;

public class AssociateFactory {
    public enum AssociateType {
        manager, employee
    }

    /**
     * It`s used to create an associate based on specific parameters
     * @return exactly what type of associate it`s desired
     */

    public static Associate createAssociate(final AssociateType type, final User user) {
        switch (type) {
            case manager: return new Manager(user);
            case employee: return new Employee(user);
            default: throw new IllegalArgumentException("That associate type is not supported");
        }
    }
}
