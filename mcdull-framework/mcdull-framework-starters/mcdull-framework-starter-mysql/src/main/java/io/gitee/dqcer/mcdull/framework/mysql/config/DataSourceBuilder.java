package io.gitee.dqcer.mcdull.framework.mysql.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 数据源建造
 *
 * @author dqcer
 * @since 2021/10/09
 */
public class DataSourceBuilder {

    private static final Logger log = LoggerFactory.getLogger(DataSourceBuilder.class);

    private static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";

    private static Method configCopyMethod = null;

    static {
        try {
            Class.forName(HIKARI_DATASOURCE);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        try {
            configCopyMethod = HikariConfig.class.getMethod("copyStateTo", HikariConfig.class);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 构建器
     *
     * @param dataSourceProperty 数据源属性
     * @return {@link DataSource}
     */
    public static DataSource builder(DataSourceProperties dataSourceProperty) {

        String poolType = dataSourceProperty.getPoolType();
        if (DataSourceProperties.DRUID.equals(poolType)) {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setUsername(dataSourceProperty.getUsername());
            druidDataSource.setPassword(dataSourceProperty.getPassword());
            druidDataSource.setUrl(dataSourceProperty.getUrl());
            druidDataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
            druidDataSource.setName("DS-Pool");
            druidDataSource.setValidationQuery("SELECT 1");
            try {
                druidDataSource.setFilters("stat, wall, log4j");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return druidDataSource;
        }

        if (DataSourceProperties.HIKARI.equals(poolType)) {
            HikariConfig config = new HikariConfig();
            config.setUsername(dataSourceProperty.getUsername());
            config.setPassword(dataSourceProperty.getPassword());
            config.setJdbcUrl(dataSourceProperty.getUrl());
            config.setPoolName("DS-Pool");

            config.setDriverClassName(dataSourceProperty.getDriverClassName());
            config.validate();
            HikariDataSource source = new HikariDataSource();
            try {
                configCopyMethod.invoke(config, source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("builder error", e);
            }
            return source;
        }
        return null;
    }
}
