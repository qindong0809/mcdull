package io.gitee.dqcer.mcdull.framework.mysql.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.gitee.dqcer.mcdull.framework.mysql.Database;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由数据来源
 *
 * @author dqcer
 * @since 2021/10/09
 */
public abstract class RoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源注册上限（含默认数据源）：用户绑定与租户路由共用，防止构造大量连接池撑爆数据库。
     */
    public static final int MAX_DATASOURCE_COUNT = 100;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>();

    @Resource
    protected DataSourceProperties dataSourceProperties;

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }

    @Override
    public void afterPropertiesSet() {
        this.setTargetDataSources(dataSourceMap);
        super.afterPropertiesSet();
    }

    public boolean containsDataSource(String lookupKey) {
        return dataSourceMap.containsKey(lookupKey);
    }

    /** 已注册数据源数量（含默认数据源），用于租户数据源上限保护 */
    public int dataSourceCount() {
        return dataSourceMap.size();
    }

    public synchronized void addDataSource(String lookupKey, DataSource dataSource) {
        dataSourceMap.put(lookupKey, dataSource);
        this.afterPropertiesSet();
    }

    @ManagedOperation(description = "remove dataSource")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "lookupKey", description = "lookupKey")
    })
    public synchronized void removeDataSource(String lookupKey) {
        dataSourceMap.remove(lookupKey);
        this.afterPropertiesSet();
    }

    @ManagedOperation(description = "get dataSourceMap")
    public Map<Object, Object> getDataSourceMap() {
        Map<Object, Object> map = new HashMap<>();
        for (Map.Entry<Object, Object> entry : dataSourceMap.entrySet()) {
            Object object = entry.getValue();
            if (object instanceof DruidDataSource) {
                map.put(entry.getKey(), ((DruidDataSource) object).getRawJdbcUrl());
            } else {
                // 打印类型
                log.error("DataSource type error. current type: {}", object.getClass().getName());
            }
        }
        return map;
    }

    public void loadDataSource(String lookupKey, Database database) {
        if (this.containsDataSource(lookupKey)) {
            log.debug("DataSource [{}] already exists", lookupKey);
            return;
        }
        DataSource dataSource = DataSourceBuilder.convertDruid(dataSourceProperties, database);
        this.addDataSource(lookupKey, dataSource);
    }

}
