

package com.github.huzekang.jdbcservice.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.github.huzekang.jdbcservice.util.Consts;
import com.github.huzekang.jdbcservice.util.SpringContextHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JdbcDataSource {


    @Value("${source.max-active:10}")
    @Getter
    private int maxActive;

    @Value("${source.initial-size:1}")
    @Getter
    private int initialSize;

    @Value("${source.min-idle:3}")
    @Getter
    private int minIdle;

    @Value("${source.max-wait:30000}")
    @Getter
    private long maxWait;

    @Value("${spring.datasource.time-between-eviction-runs-millis}")
    @Getter
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.min-evictable-idle-time-millis}")
    @Getter
    private long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.test-while-idle}")
    @Getter
    private boolean testWhileIdle;

    @Value("${spring.datasource.test-on-borrow}")
    @Getter
    private boolean testOnBorrow;

    @Value("${spring.datasource.test-on-return}")
    @Getter
    private boolean testOnReturn;

    @Value("${source.break-after-acquire-failure:true}")
    @Getter
    private boolean breakAfterAcquireFailure;

    @Value("${source.connection-error-retry-attempts:0}")
    @Getter
    private int connectionErrorRetryAttempts;

    @Value("${source.query-timeout:600000}")
    @Getter
    private int queryTimeout;

    private static volatile Map<String, DruidDataSource> dataSourceMap = new HashMap<>();

    public synchronized void removeDatasource(String jdbcUrl, String username, String password, String version, boolean isExt) {
        String key = SourceUtils.getKey(jdbcUrl, username, password, version, isExt);

        if (dataSourceMap.containsKey(key)) {
            DruidDataSource druidDataSource = dataSourceMap.get(key);
            druidDataSource.close();
            dataSourceMap.remove(key);
        }
    }

    /**
     * 根据jdbc 用户名 账号等信息。
     * 初始化一个druid数据源
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @param database  这个参数是用于定位拓展驱动，区分驱动存放文件夹用的
     * @param version
     * @param isExt
     * @return
     * @throws SourceException
     */
    public synchronized DruidDataSource getDataSource(String jdbcUrl, String username, String password, String database, String version, boolean isExt) throws SourceException {
        String key = SourceUtils.getKey(jdbcUrl, username, password, version, isExt);

        if (dataSourceMap.containsKey(key) && dataSourceMap.get(key) != null) {
            DruidDataSource druidDataSource = dataSourceMap.get(key);
            if (!druidDataSource.isClosed()) {
                return druidDataSource;
            } else {
                // 检查以及失效则从map中去掉
                dataSourceMap.remove(key);
            }
        }

        DruidDataSource instance = new DruidDataSource();

        if (StringUtils.isEmpty(version) || !isExt || Consts.JDBC_DATASOURCE_DEFAULT_VERSION.equals(version)) {
            // 没有指定版本的，不是拓展标识的，即不是用户自行拓展的的处理方式
            String className = SourceUtils.getDriverClassName(jdbcUrl, null);
            try {
                Class<?> aClass = Class.forName(className);
                if (null == aClass) {
                    throw new SourceException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
                }
            } catch (ClassNotFoundException e) {
                throw new SourceException("Unable to get driver instance for jdbcUrl: " + jdbcUrl);
            }

            instance.setDriverClassName(className);

        } else {
            // 用户自行拓展的的处理方式
            String path = SpringContextHolder.getBean(ServerUtils.class).getBasePath() + String.format(Consts.PATH_EXT_FORMATER, database, version);
            instance.setDriverClassLoader(ExtendedJdbcClassLoader.getExtJdbcClassLoader(path));
        }

        instance.setUrl(jdbcUrl.trim());
        instance.setUsername(jdbcUrl.toLowerCase().contains(DataTypeEnum.ELASTICSEARCH.getFeature()) ? null : username);
        instance.setPassword((jdbcUrl.toLowerCase().contains(DataTypeEnum.PRESTO.getFeature()) || jdbcUrl.toLowerCase().contains(DataTypeEnum.ELASTICSEARCH.getFeature())) ?
                null : password);
        instance.setInitialSize(initialSize);
        instance.setMinIdle(minIdle);
        instance.setMaxActive(maxActive);
        instance.setMaxWait(maxWait);
        instance.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        instance.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        instance.setTestWhileIdle(false);
        instance.setTestOnBorrow(testOnBorrow);
        instance.setTestOnReturn(testOnReturn);
        instance.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
        instance.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
//        instance.setQueryTimeout(queryTimeout / 1000);

        try {
            instance.init();
        } catch (Exception e) {
            log.error("Exception during pool initialization", e);
            throw new SourceException(e.getMessage());
        }
        dataSourceMap.put(key, instance);
        return instance;
    }
}
