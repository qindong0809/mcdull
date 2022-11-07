package com.dqcer.mcdull.framework.mysql.config;

import com.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由数据来源
 *
 * @author dqcer
 * @date 2021/10/09
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private DataSourceProperties properties;

    private final Map<Object, Object> objectObjectMap  = new ConcurrentHashMap<>();


    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String peek = DynamicContextHolder.peek();
        if (null == peek || peek.trim().length() == 0) {
            return properties.getDefaultName();
        }
        DataSource dataSource = dataSourceMap.get(peek);
        if (null == dataSource) {
            loadDataSource(peek, DataSourceBuilder.builder(properties));
            dataSource = dataSourceMap.get(peek);
            if (null == dataSource) {
                log.error("determineCurrentLookupKey key: {}", peek);
                throw new IllegalArgumentException(peek + " 库未找到" );
            }
        }
        return peek;
    }


    /**
     * 加载数据来源
     *
     * @param lookupKey  查找关键
     * @param dataSource 数据源
     */
    public void loadDataSource(String lookupKey, DataSource dataSource) {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();


            /*
                select * from information_schema.TABLES
                where TABLE_NAME = '需要查询的数据表名';
             */

            ResultSet rs = stmt.executeQuery(properties.getTenantSql() + " where tenant_id = " + lookupKey);

            while (rs.next()) {
                String name = rs.getString("database");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String host = rs.getString("host");
                String port = rs.getString("port");
                String param = rs.getString("param");
                dataSourceProperties.setUsername(username);
                dataSourceProperties.setPassword(password);
                dataSourceProperties.setUrl("jdbc:mysql://" + host + ":" + port + "/" + name + param);
                dataSourceProperties.setDriverClassName(properties.getDriverClassName());
            }
        } catch (Exception e) {
            log.error("初始化数据库失败", e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
        }

        if (null == dataSourceProperties.getDriverClassName()) {
            // 表示未进入到while中
            throw new IllegalArgumentException("租户库: [" + lookupKey + "] 找不到，请检查该库是否存在或long类型的精度丢失");
        }

        DataSource ds = DataSourceBuilder.builder(dataSourceProperties);
        dataSourceMap.put(lookupKey, dataSource);

        objectObjectMap.put(lookupKey, ds);
        setTargetDataSources(objectObjectMap);
        super.afterPropertiesSet();
    }

}
