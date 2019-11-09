package com.github.huzekang.jdbcservice;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.github.huzekang.jdbcservice.service.DataTypeEnum;
import com.github.huzekang.jdbcservice.service.JdbcDataSource;
import com.github.huzekang.jdbcservice.service.SourceUtils;
import com.github.huzekang.jdbcservice.service.sql.Source;
import com.github.huzekang.jdbcservice.service.sql.SourceConfig;
import com.github.huzekang.jdbcservice.service.sql.SourceCreate;
import com.github.huzekang.jdbcservice.service.sql.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 测试底层实现JdbcDataSource和SourceUtils
 *
 * @author: huzekang
 * @create: 2019-11-08 14:48
 **/
@Slf4j
@SpringBootTest(classes = JdbcServiceApplication.class)
public class SourceUtilsAndJdbcDataSourceTest {

    @Autowired
    JdbcDataSource jdbcDataSource;

    @Autowired
    SourceUtils sourceUtils;



    @Test
    public void getDatasource() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306?serverTimezone=UTC  ";
        String username = "root";
        String password = "root";
        String database = "foodmart";
        String version = "default";
        boolean isExt = false;
        DruidDataSource dataSource = jdbcDataSource.getDataSource(jdbcUrl, username, password, database, version, isExt);
        System.out.println(dataSource);
        System.out.println(dataSource.getConnection().getCatalog());

        String dataSourceName = SourceUtils.getDataSourceName(jdbcUrl);
        System.out.println(dataSourceName);

        String driverClassName = SourceUtils.getDriverClassName(jdbcUrl, null);
        System.out.println(driverClassName);

        System.out.println(SourceUtils.isSupportedDatasource(jdbcUrl));
        System.out.println(SourceUtils.checkDriver(DataTypeEnum.urlOf(jdbcUrl).getFeature(), jdbcUrl, null, false));


    }


    @Test
    public void getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/foodmart?serverTimezone=UTC  ";
        String username = "root";
        String password = "root";
        String database = null;
        String version = null;
        boolean isExt = false;
        Connection connection = sourceUtils.getConnection(jdbcUrl, username, password, null, null, isExt);
        log.info("catalog:{},metadata:{},typeMap:{},clientInfo:{}",
                connection.getCatalog(),
                JSONUtil.toJsonStr(connection.getMetaData()),
                JSONUtil.toJsonStr(connection.getTypeMap()),
                JSONUtil.toJsonStr(connection.getClientInfo())
                );
    }


}

