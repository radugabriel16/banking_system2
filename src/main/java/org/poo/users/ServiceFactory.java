package org.poo.users;

public class ServiceFactory {
    public enum ServiceType {
        Standard, Student, Silver, Gold
    }

    public static ServicePlan createService(ServiceType type) {
        switch (type) {
            case Standard: return new Standard();
            case Student: return new Student();
            case Silver: return new Silver();
            case Gold: return new Gold();
            default: throw new IllegalArgumentException("That service plan is not supported");
        }
    }
}