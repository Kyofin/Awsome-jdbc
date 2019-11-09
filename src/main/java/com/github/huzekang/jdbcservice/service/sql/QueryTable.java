package com.github.huzekang.jdbcservice.service.sql;

import com.github.huzekang.jdbcservice.util.Consts;
import lombok.Data;

@Data
public class QueryTable {
    private String name;
    private String type;

    public QueryTable(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type == null ? Consts.EMPTY : type;
    }
}
