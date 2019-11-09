package com.github.huzekang.jdbcservice.service.sql;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建数据源时前端传递的dto
 */
@Data
@NotNull(message = "source info cannot be null")
public class SourceCreate {

    @NotBlank(message = "source name cannot be EMPTY")
    private String name;

    private String description;

    @NotBlank(message = "souce type cannot be EMPTY")
    private String type;

    @Min(value = 1L, message = "Invalid project id")
    private Long projectId;

    @NotNull(message = "source config cannot be null")
    private SourceConfig config;
}