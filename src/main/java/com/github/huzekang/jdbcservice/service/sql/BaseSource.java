

package com.github.huzekang.jdbcservice.service.sql;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class BaseSource extends RecordInfo<Source> {


    public abstract String getJdbcUrl();


    public abstract String getUsername();

    public abstract String getPassword();

    public abstract String getDatabase();

    public abstract String getDbVersion();

    public abstract boolean isExt();
}
