

package com.github.huzekang.jdbcservice.service.sql;


import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.huzekang.jdbcservice.util.Consts;
import com.github.huzekang.jdbcservice.service.SourceUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
/**
 * 对应数据库中source表的实体类
 */
public class Source extends BaseSource {
    private Long id;

    private String name;

    private String description;

    private String type;

    private Long projectId;

    @JSONField(serialize = false)
    private String config;

    /**
     * 从config中获取jdbcUrl
     * <p>
     * json key： url
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public String getJdbcUrl() {
        String url = null;
        if (null == config) {
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            url = jsonObject.getString("url");
        } catch (Exception e) {
            log.error("get jdbc url from source config, {}", e.getMessage());
        }
        return url;
    }

    /**
     * 从config中获取jdbc username
     * <p>
     * json key: user
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        String username = null;
        if (null == config) {
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            username = jsonObject.getString("username");
        } catch (Exception e) {
            log.error("get jdbc user from source config, {}", e.getMessage());
        }
        return username;
    }

    /**
     * 从config中获取 jdbc password
     * <p>
     * json key: password
     *
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        String password = null;
        if (null == config) {
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            password = jsonObject.getString("password");
        } catch (Exception e) {
            log.error("get jdbc password from source config, {}", e.getMessage());
        }
        return password;
    }

    @Override
    @JSONField(serialize = false)
    public String getDatabase() {
        return SourceUtils.getDataSourceName(getJdbcUrl());
    }

    @Override
    @JSONField(serialize = false)
    public String getDbVersion() {
        String versoin = null;
        if (null == config) {
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            versoin = jsonObject.getString("version");
            if (Consts.JDBC_DATASOURCE_DEFAULT_VERSION.equals(versoin)) {
                return null;
            }
        } catch (Exception e) {
        }
        return StringUtils.isEmpty(versoin) ? null : versoin;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isExt() {
        boolean ext = false;
        if (null == config) {
            return false;
        }
        if (getDbVersion() == null) {
            ext = false;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            ext = jsonObject.getBooleanValue("ext");
        } catch (Exception e) {
        }
        return ext;
    }

    @JSONField(serialize = false)
    public String getConfigParams() {
        String params = null;
        if (null == config) {
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(this.config);
            params = jsonObject.getString("parameters");
        } catch (Exception e) {
            log.error("get jdbc parameters from source config, {}", e.getMessage());
        }
        return params;
    }


    @Override
    public String toString() {
        return "Source{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", projectId=" + projectId +
                ", config='" + config + '\'' +
                '}';
    }
}