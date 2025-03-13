package com.imbuka.customer_service.enums;

import lombok.Getter;

@Getter
public enum Permissions {
    READ("read"),

    CUSTOMER_READ("customer:read"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_DELETE("customer:delete"),

    ADMIN_READ("admin:read"),

    ADMIN_UPDATE("admin:update"),

    ADMIN_CREATE("admin:create"),

    ADMIN_DELETE("admin:delete"),
    ;

    private final String permissions;

    Permissions(String permissions) {
        this.permissions = permissions;
    }
}
