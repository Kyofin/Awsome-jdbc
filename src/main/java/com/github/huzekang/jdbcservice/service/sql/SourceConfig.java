

package com.github.huzekang.jdbcservice.service.sql;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SourceConfig {

    private String username;

    private String password;

    @NotBlank(message = "connection url cannot be EMPTY")
    private String url;

    private String parameters;

    private String version;

    private boolean isExt;


    public SourceConfig(Source source) {
        this.username = source.getUsername();
        this.password = source.getPassword();
        this.url = source.getJdbcUrl();
        this.parameters = source.getConfigParams();
        this.version = source.getDbVersion();
        this.isExt = source.isExt();
    }

    public SourceConfig() {
    }
}
