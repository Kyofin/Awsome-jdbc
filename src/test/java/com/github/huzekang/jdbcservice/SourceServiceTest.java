package com.github.huzekang.jdbcservice;

import com.github.huzekang.jdbcservice.core.SourceService;
import com.github.huzekang.jdbcservice.enums.UploadModeEnum;
import com.github.huzekang.jdbcservice.model.QueryColumn;
import com.github.huzekang.jdbcservice.model.Source;
import com.github.huzekang.jdbcservice.model.SourceDataUpload;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
/**
 * @program: jdbc-service
 * @author: huzekang
 * @create: 2019-11-08 18:22
 **/
@Slf4j
@SpringBootTest(classes = JdbcServiceApplication.class)
public class SourceServiceTest {

    @Autowired
    SourceService sourceService;

    @Test
    public void createTable() {
        // 定义mysql数据源
        Source source = new Source();
        source.setConfig("{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost:3306/test?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"}");
        source.setName("测试数据源");

        // 定义创建表meta
        SourceDataUpload sourceDataUpload = new SourceDataUpload();
        sourceDataUpload.setPrimaryKeys("id");
        sourceDataUpload.setIndexKeys("age");
        sourceDataUpload.setTableName("t_patient2");
        sourceDataUpload.setMode(UploadModeEnum.REPLACE.getMode());

        Set<QueryColumn> queryColumns = new HashSet<>();
        queryColumns.add(new QueryColumn("id", "BIGINT"));
        queryColumns.add(new QueryColumn("age", "INTEGER"));


        sourceService.createTable(queryColumns,sourceDataUpload,source);
    }

}