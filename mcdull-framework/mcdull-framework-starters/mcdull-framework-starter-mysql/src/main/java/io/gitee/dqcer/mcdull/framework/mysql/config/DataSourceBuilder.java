package io.gitee.dqcer.mcdull.framework.mysql.config;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.mysql.Database;
import io.gitee.dqcer.mcdull.framework.mysql.properties.DataSourceProperties;
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
    public static DataSource builder(DataSourceProperties dataSourceProperty, Database database) {

        String poolType = dataSourceProperty.getPoolType();
        LogHelp.info(log, "{} pool init", poolType);
        if (DataSourceProperties.DRUID.equals(poolType)) {
            DruidDataSource druidDataSource = convertDruid(dataSourceProperty, database);
            druidDataSource.setName("DS-Pool");
            return druidDataSource;
        }

        if (DataSourceProperties.HIKARI.equals(poolType)) {
            HikariConfig config = new HikariConfig();
            config.setUsername(database.getUsername());
            config.setPassword(database.getPassword());
            config.setJdbcUrl(database.getUrl());
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

    public static DruidDataSource convertDruid(DataSourceProperties properties, Database database) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(properties.getDriverClassName());
        ds.setInitialSize(properties.getInitialSize());
        ds.setMinIdle(properties.getMinIdle());
        ds.setMaxActive(properties.getMaxActive());
        ds.setMaxWait(properties.getMaxWait());
        ds.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(properties.getValidationQuery());
        ds.setValidationQueryTimeout(properties.getValidationQueryTimeout());
        ds.setTestWhileIdle(properties.isTestWhileIdle());
        ds.setTestOnBorrow(properties.isTestOnBorrow());
        ds.setTestOnReturn(properties.isTestOnReturn());
        ds.setConnectionInitSqls(ListUtil.of("set names utf8mb4;"));

        try {
            ds.setFilters(properties.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter: {}", properties.getFilters(), e);
            throw new RuntimeException(e);
        }

        ds.setUrl(database.getUrl() +
                Convert.toStr(properties.getUrlSuffix(), "") +
                "&allowMultiQueries=" + properties.getAllowMultiQueries());
        ds.setUsername(database.getUsername());
        // todo 密码加密
        ds.setPassword(database.getPassword());

        return ds;
    }
}
