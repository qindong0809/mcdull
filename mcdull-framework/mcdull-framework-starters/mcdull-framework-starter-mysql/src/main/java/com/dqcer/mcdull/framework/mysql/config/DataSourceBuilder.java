package com.dqcer.mcdull.framework.mysql.config;

import com.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 数据源建造
 *
 * @author dqcer
 * @version 2021/10/09
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

}
