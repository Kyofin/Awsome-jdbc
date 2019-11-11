package com.github.huzekang.jdbcservice.model;

import com.github.huzekang.jdbcservice.util.Consts;
import lombok.Data;

@Data
public class QueryColumn {
    private String name;
    private String type;

    public QueryColumn(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type == null ? Consts.EMPTY : type;
    }
}
