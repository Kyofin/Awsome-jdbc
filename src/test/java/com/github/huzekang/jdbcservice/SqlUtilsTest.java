package com.github.huzekang.jdbcservice;

import cn.hutool.json.JSONUtil;
import com.github.huzekang.jdbcservice.enums.TableInfo;
import com.github.huzekang.jdbcservice.model.*;
import com.github.huzekang.jdbcservice.core.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * @program: jdbc-service
 * @author: huzekang
 * @create: 2019-11-08 18:22
 **/
@Slf4j
@SpringBootTest(classes = JdbcServiceApplication.class)
public class SqlUtilsTest {

    @Autowired
    SqlUtils sqlUtils;
    /**
     * 业务层直接调用核心类sqlutils
     */
    @Test
    public void testSqlUtilConnect() {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        String sourceCreateJson2 = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost/foodmart2?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceCreate sourceCreate2 = JSONUtil.toBean(sourceCreateJson2,SourceCreate.class);

        //测试连接
        SourceConfig config = sourceCreate.getConfig();
        SourceConfig config2 = sourceCreate2.getConfig();
        // 根据jdbc连接配置获取一个组装好的sqlUtils
        boolean testConnection = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        ).testConnection();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        boolean testConnection2 = sqlUtils.init(
                config2.getUrl(),
                config2.getUsername(),
                config2.getPassword(),
                config2.getVersion(),
                config2.isExt()
        ).testConnection();

        System.out.println(testConnection);
        System.out.println(testConnection2);


    }


    /**
     * 获取一个jdbc连接中的数据库列表
     */
    @Test
    public void getDatabases() {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        // 使用加入jdbc连接后的sqlUtil
        List<String> databases = assemblySqlUtils.getDatabases();
        System.out.println(databases);


    }


    /**
     * 获取指定数据库的所有表
     */
    @Test
    public void getTableList() {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        // 使用加入jdbc连接后的sqlUtil
        List<QueryTable> tableList = assemblySqlUtils.getTableList("dhq_dev");
        System.out.println(tableList);


    }


    /**
     * 获取指定表的信息
     */
    @Test
    public void getTableInfo() {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        // 使用加入jdbc连接后的sqlUtil
        TableInfo tableInfo = assemblySqlUtils.getTableInfo("yiboard", "chart");
        System.out.println(tableInfo);


    }


    /**
     * 获取sql中的字段信息
     */
    @Test
    public void getSqlColumns() {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        // 使用加入jdbc连接后的sqlUtil
        List<QueryColumn> columns = assemblySqlUtils.getColumns("select * from yiboard.chart");
        System.out.println(columns);


    }

    /**
     * 获取sql查询的分页数据
     * @throws Exception
     */
    @Test
    public void getSqlDataPage() throws Exception {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        PaginateWithQueryColumns paginateWithQueryColumns = assemblySqlUtils
                .query4Paginate("select id ,user_id ,chart_name from yiboard.chart",
                2, 4, 100, 10, null);
        System.out.println(paginateWithQueryColumns.getColumns());
        System.out.println(paginateWithQueryColumns.getResultList() );
    }

    /**
     * 获取sql查询的所有数据
     * @throws Exception
     */
    @Test
    public void getSqlData() throws Exception {
        String sourceCreateJson = "{\"id\":0,\"name\":\"test\",\"type\":\"jdbc\",\"description\":\"\",\"config\":{\"username\":\"root\",\"password\":\"eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT\",\"url\":\"jdbc:mysql://localhost?serverTimezone=UTC\",\"parameters\":\"\",\"ext\":false,\"version\":\"\"},\"projectId\":17}";
        SourceCreate sourceCreate = JSONUtil.toBean(sourceCreateJson,SourceCreate.class);
        SourceConfig config = sourceCreate.getConfig();

        // 根据jdbc连接配置获取一个组装好的sqlUtils
        SqlUtils assemblySqlUtils = sqlUtils.init(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getVersion(),
                config.isExt()
        );

        List<Map<String, Object>> mapList = assemblySqlUtils
                .query4List("select id ,user_id ,chart_name from yiboard.chart", -1);
        System.out.println(mapList);
    }

}
