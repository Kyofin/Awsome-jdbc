

package com.github.huzekang.jdbcservice.service.sql;

public enum LogNameEnum {
    BUSINESS_SQL("BUSINESS_SQL"),
    BUSINESS_OPERATION("BUSINESS_OPERATION");

    private String name;

    LogNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
