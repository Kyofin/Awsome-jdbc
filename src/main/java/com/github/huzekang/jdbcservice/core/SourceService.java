package com.github.huzekang.jdbcservice.core;

import com.github.huzekang.jdbcservice.enums.UploadModeEnum;
import com.github.huzekang.jdbcservice.exception.ServerException;
import com.github.huzekang.jdbcservice.model.QueryColumn;
import com.github.huzekang.jdbcservice.model.Source;
import com.github.huzekang.jdbcservice.model.SourceDataUpload;
import com.github.huzekang.jdbcservice.util.Consts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.Set;

/**
 * @program: jdbc-service
 * @author: huzekang
 * @create: 2019-11-19 16:35
 **/
@Service
@Slf4j
public class SourceService {

    @Autowired
    SqlUtils sqlUtils;

    /**
     * 建表
     *
     * @param fileds
     * @param sourceDataUpload
     * @param source
     * @throws ServerException
     */
    public void createTable(Set<QueryColumn> fileds, SourceDataUpload sourceDataUpload, Source source) throws ServerException {

        if (CollectionUtils.isEmpty(fileds)) {
            throw new ServerException("there is have not any fileds");
        }

        SqlUtils sqlUtils = this.sqlUtils.init(source);

        STGroup stg = new STGroupFile(Consts.SQL_TEMPLATE);

        String sql = null;

        if (sourceDataUpload.getMode() == UploadModeEnum.REPLACE.getMode()) {
            ST st = stg.getInstanceOf("createTable");
            st.add("tableName", sourceDataUpload.getTableName());
            st.add("fields", fileds);
            st.add("primaryKeys", StringUtils.isEmpty(sourceDataUpload.getPrimaryKeys()) ? null : sourceDataUpload.getPrimaryKeys().split(","));
            st.add("indexKeys", sourceDataUpload.getIndexList());
            sql = st.render();
            String dropSql = "DROP TABLE IF EXISTS `" + sourceDataUpload.getTableName() + "`";
            sqlUtils.jdbcTemplate().execute(dropSql);
            log.info("drop table sql : {}", dropSql);
        } else {
            boolean tableIsExist = sqlUtils.tableIsExist(sourceDataUpload.getTableName());
            if (sourceDataUpload.getMode() == UploadModeEnum.NEW.getMode()) {
                if (!tableIsExist) {
                    ST st = stg.getInstanceOf("createTable");
                    st.add("tableName", sourceDataUpload.getTableName());
                    st.add("fields", fileds);
                    st.add("primaryKeys", sourceDataUpload.getPrimaryKeys());
                    st.add("indexKeys", sourceDataUpload.getIndexList());

                    sql = st.render();
                } else {
                    throw new ServerException("table " + sourceDataUpload.getTableName() + " is already exist");
                }
            } else {
                if (!tableIsExist) {
                    throw new ServerException("table " + sourceDataUpload.getTableName() + " is not exist");
                }
            }
        }

        log.info("create table sql : {}", sql);
        try {
            if (!StringUtils.isEmpty(sql)) {
                sqlUtils.jdbcTemplate().execute(sql);
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
