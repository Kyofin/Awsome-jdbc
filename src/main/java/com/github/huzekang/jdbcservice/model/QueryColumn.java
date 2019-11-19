package com.github.huzekang.jdbcservice.model;

import com.github.huzekang.jdbcservice.util.Consts;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryColumn {
    private String name;
    private String type;
    /**
     * 字段的注释
     */
    private String remarks;

    /**
     * 字段长度
     */
    private Integer columnSize;

    public QueryColumn(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type == null ? Consts.EMPTY : type;
    }
}
