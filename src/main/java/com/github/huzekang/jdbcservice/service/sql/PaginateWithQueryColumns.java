package com.github.huzekang.jdbcservice.service.sql;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PaginateWithQueryColumns extends Paginate<Map<String, Object>> {
    List<QueryColumn> columns;
}
