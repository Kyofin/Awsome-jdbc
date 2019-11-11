

package com.github.huzekang.jdbcservice.model;

import com.github.huzekang.jdbcservice.enums.DataTypeEnum;
import com.github.huzekang.jdbcservice.util.Consts;
import lombok.Getter;

import java.util.List;

@Getter
public class DatasourceType {
    private String name;
    private String prefix;
    private List<String> versions;

    public DatasourceType(String name, List<String> versions) {
        this.name = name;
        this.prefix = name.equalsIgnoreCase(DataTypeEnum.ORACLE.getFeature()) ? Consts.ORACLE_JDBC_PREFIX : String.format(Consts.JDBC_PREFIX_FORMATER, name);
        this.versions = versions;
    }
}
