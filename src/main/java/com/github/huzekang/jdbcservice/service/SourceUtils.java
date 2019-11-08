

package com.github.huzekang.jdbcservice.service;

import com.alibaba.druid.util.StringUtils;
import com.github.huzekang.jdbcservice.util.Consts;
import com.github.huzekang.jdbcservice.util.MD5Util;
import com.github.huzekang.jdbcservice.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;

@Slf4j
@Component
public class SourceUtils {

    @Autowired
    private JdbcDataSource jdbcDataSource;

    public SourceUtils(JdbcDataSource jdbcDataSource) {
        this.jdbcDataSource = jdbcDataSource;
    }

    /**
     * 获取数据源
     *
     * @param jdbcUrl
     * @param userename
     * @param password
     * @param database
     * @param version
     * @param isExt
     * @return
     * @throws SourceException
     */
   public DataSource getDataSource(String jdbcUrl, String userename, String password, String database, String version, boolean isExt) throws SourceException {
        if (jdbcUrl.toLowerCase().contains(DataTypeEnum.ELASTICSEARCH.getDesc().toLowerCase())) {
            throw new ServerException("不支持es");
        } else {
            return jdbcDataSource.getDataSource(jdbcUrl, userename, password, database, version, isExt);
        }
    }

    public Connection getConnection(String jdbcUrl, String username, String password, String database, String version, boolean isExt) throws SourceException {
        DataSource dataSource = getDataSource(jdbcUrl, username, password, database, version, isExt);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception e) {
            connection = null;
        }
        try {
            if (null == connection || connection.isClosed()) {
                log.info("connection is closed, retry get connection!");
                releaseDataSource(jdbcUrl, username, password, version, isExt);
                dataSource = getDataSource(jdbcUrl, username, password, database, version, isExt);
                connection = dataSource.getConnection();
            }
        } catch (Exception e) {
            log.error("create connection error, jdbcUrl: {}", jdbcUrl);
            throw new SourceException("create connection error, jdbcUrl: " + jdbcUrl);
        }

        try {
            if (!connection.isValid(5)) {
                log.info("connection is invalid, retry get connection!");
                releaseDataSource(jdbcUrl, username, password, version, isExt);
                connection = null;
            }
        } catch (Exception e) {
        }

        if (null == connection) {
            try {
                dataSource = getDataSource(jdbcUrl, username, password, database, version, isExt);
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                log.error("create connection error, jdbcUrl: {}", jdbcUrl);
                throw new SourceException("create connection error, jdbcUrl: " + jdbcUrl);
            }
        }

        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
                connection = null;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("connection close error", e.getMessage());
            }
        }
    }


    public static void closeResult(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkDriver(String dataSourceName, String jdbcUrl, String version, boolean isExt) {
        if (!StringUtils.isEmpty(dataSourceName) && LoadSupportDataSourceRunner.getSupportDatasourceMap().containsKey(dataSourceName)) {
            if (isExt && !StringUtils.isEmpty(version) && !Consts.JDBC_DATASOURCE_DEFAULT_VERSION.equals(version)) {
                String path = SpringContextHolder.getBean(ServerUtils.class).getBasePath() + String.format(Consts.PATH_EXT_FORMATER, dataSourceName, version);
                ExtendedJdbcClassLoader extendedJdbcClassLoader = ExtendedJdbcClassLoader.getExtJdbcClassLoader(path);
                CustomDataSource dataSource = CustomDataSourceUtils.getInstance(jdbcUrl, version);
                try {
                    assert extendedJdbcClassLoader != null;
                    Class<?> aClass = extendedJdbcClassLoader.loadClass(dataSource.getDriver());
                    if (null == aClass) {
                        throw new SourceException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
                    }
                } catch (NullPointerException en) {
                    throw new ServerException("JDBC driver is not found: " + dataSourceName + ":" + version);
                } catch (ClassNotFoundException ex) {
                    throw new SourceException("Unable to get driver instance: " + jdbcUrl);
                }
            } else {
                if (DataTypeEnum.ELASTICSEARCH.getDesc().equals(dataSourceName)) {
                    return true;
                } else {
                    try {
                        String className = getDriverClassName(jdbcUrl, null);
                        Class<?> aClass = Class.forName(className);
                        if (null == aClass) {
                            throw new SourceException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
                        }
                    } catch (Exception e) {
                        throw new SourceException("Unable to get driver instance: " + jdbcUrl);
                    }
                }
            }
            return true;
        } else {
            throw new SourceException("Not supported data type: jdbcUrl=" + jdbcUrl);
        }
    }

    public static String isSupportedDatasource(String jdbcUrl) {
        String dataSourceName = getDataSourceName(jdbcUrl);
        if (StringUtils.isEmpty(dataSourceName)) {
            throw new SourceException("Not supported data type: jdbcUrl=" + jdbcUrl);
        }
        if (!LoadSupportDataSourceRunner.getSupportDatasourceMap().containsKey(dataSourceName)) {
            throw new SourceException("Not supported data type: jdbcUrl=" + jdbcUrl);
        }
        return dataSourceName;
    }

    public static String getDataSourceName(String jdbcUrl) {
        String dataSourceName = null;
        jdbcUrl = jdbcUrl.replaceAll(Consts.NEW_LINE_CHAR, Consts.EMPTY).replaceAll(Consts.SPACE, Consts.EMPTY).trim().toLowerCase();
        Matcher matcher = Consts.PATTERN_JDBC_TYPE.matcher(jdbcUrl);
        if (matcher.find()) {
            dataSourceName = matcher.group().split(Consts.COLON)[1];
        }
        return dataSourceName;
    }

    /**
     * 根据jdbcurl获取数据源驱动类
     * 1. 先用官方的DriverManager获取
     * 2. 找不到再用自定义的数据源枚举类获取
     *
     * @param jdbcUrl
     * @param version
     * @return
     */
    public static String getDriverClassName(String jdbcUrl, String version) {
        String className = null;
        try {
            className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
        } catch (SQLException e) {
        }
        if (StringUtils.isEmpty(className)) {
            DataTypeEnum dataTypeEnum = DataTypeEnum.urlOf(jdbcUrl);
            CustomDataSource customDataSource = null;
            if (null == dataTypeEnum) {
                try {
                    customDataSource = CustomDataSourceUtils.getInstance(jdbcUrl, version);
                } catch (Exception e) {
                    throw new SourceException(e.getMessage());
                }
            }

            if (null == dataTypeEnum && null == customDataSource) {
                throw new SourceException("Not supported data type: jdbcUrl=" + jdbcUrl);
            }
            className = null != dataTypeEnum && !StringUtils.isEmpty(dataTypeEnum.getDriver()) ? dataTypeEnum.getDriver() : customDataSource.getDriver().trim();
        }
        return className;
    }


    /**
     * 释放失效数据源
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @param dbVersion
     * @param isExt
     * @return
     */
    public void releaseDataSource(String jdbcUrl, String username, String password, String dbVersion, boolean isExt) {
        if (jdbcUrl.toLowerCase().contains(DataTypeEnum.ELASTICSEARCH.getDesc().toLowerCase())) {
//            ESDataSource.removeDataSource(jdbcUrl, username, password);
        } else {
            jdbcDataSource.removeDatasource(jdbcUrl, username, password, dbVersion, isExt);
        }
    }

    public static String getKey(String jdbcUrl, String username, String password, String version, boolean isExt) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(username)) {
            sb.append(username);
        }
        if (!StringUtils.isEmpty(password)) {
            sb.append(Consts.COLON).append(password);
        }
        sb.append(Consts.AT_SYMBOL).append(jdbcUrl.trim());
        if (isExt && !StringUtils.isEmpty(version)) {
            sb.append(Consts.COLON).append(version);
        }

        return MD5Util.encode(sb.toString());
    }
}
