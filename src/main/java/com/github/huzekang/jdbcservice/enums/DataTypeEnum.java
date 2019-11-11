

package com.github.huzekang.jdbcservice.enums;

import com.github.huzekang.jdbcservice.exception.SourceException;
import com.github.huzekang.jdbcservice.util.Consts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum DataTypeEnum {

    MYSQL("mysql", "mysql", "com.mysql.jdbc.Driver", "`", "`", "'", "'"),
    
    POSTGRESQL("postgresql", "postgre", "org.postgresql.Driver", "`", "`", "'", "'"),


    ORACLE("oracle", "oracle", "oracle.jdbc.driver.OracleDriver", "\"", "\"", "\"", "\""),

    SQLSERVER("sqlserver", "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "\"", "\"", "\"", "\""),

    H2("h2", "h2", "org.h2.Driver", "`", "`", "\"", "\""),

    PHOENIX("phoenix", "hbase phoenix", "org.apache.phoenix.jdbc.PhoenixDriver", "", "", "\"", "\""),

    MONGODB("mongo", "mongodb", "mongodb.jdbc.MongoDriver", "`", "`", "\"", "\""),

    ELASTICSEARCH("elasticsearch", "elasticsearch", "nl.anchormen.sql4es.jdbc.ESDriver", "", "", "'", "'"),

    PRESTO("presto", "presto", "com.facebook.presto.jdbc.PrestoDriver", "\"", "\"", "\"", "\""),

    MOONBOX("moonbox", "moonbox", "moonbox.jdbc.MbDriver", "`", "`", "`", "`"),

    CASSANDRA("cassandra", "cassandra", "com.github.adejanovski.cassandra.jdbc.CassandraDriver", "", "", "'", "'"),

    CLICKHOUSE("clickhouse", "clickhouse", "ru.yandex.clickhouse.ClickHouseDriver", "", "", "\"", "\""),

    KYLIN("kylin", "kylin", "org.apache.kylin.jdbc.Driver", "\"", "\"", "\"", "\""),

    VERTICA("vertica", "vertica", "com.vertica.jdbc.Driver", "", "", "'", "'"),

    HANA("sap", "sap hana", "com.sap.db.jdbc.Driver", "", "", "'", "'"),

    IMPALA("impala", "impala", "com.cloudera.impala.jdbc41.Driver", "", "", "'", "'");


    /**
     * 在jdbcrul中的特征标记
     */
    private String feature;
    private String desc;
    private String driver;
    /**
     * sql中表名字段之类的前缀
     */
    private String keywordPrefix;
    private String keywordSuffix;
    /**
     * sql中表名字段之类别名的前缀
     */
    private String aliasPrefix;
    private String aliasSuffix;

    DataTypeEnum(String feature, String desc, String driver, String keywordPrefix, String keywordSuffix, String aliasPrefix, String aliasSuffix) {
        this.feature = feature;
        this.desc = desc;
        this.driver = driver;
        this.keywordPrefix = keywordPrefix;
        this.keywordSuffix = keywordSuffix;
        this.aliasPrefix = aliasPrefix;
        this.aliasSuffix = aliasSuffix;
    }

    public static DataTypeEnum urlOf(String jdbcUrl) throws SourceException {
        String url = jdbcUrl.toLowerCase().trim();
        for (DataTypeEnum dataTypeEnum : values()) {
            if (url.startsWith(String.format(Consts.JDBC_PREFIX_FORMATER, dataTypeEnum.feature))) {
                return dataTypeEnum;
            }
        }
        return null;
    }

    public String getFeature() {
        return feature;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriver() {
        return driver;
    }

    public String getKeywordPrefix() {
        return keywordPrefix;
    }

    public String getKeywordSuffix() {
        return keywordSuffix;
    }

    public String getAliasPrefix() {
        return aliasPrefix;
    }

    public String getAliasSuffix() {
        return aliasSuffix;
    }
}
