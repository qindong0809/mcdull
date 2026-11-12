package io.gitee.dqcer.mcdull.framework.scheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 调度表库数据源提供者。
 *
 * <p>不作为 {@link DataSource} 类型的 Bean 暴露，避免与业务模块的动态路由数据源冲突；
 * 同时保证 db-scheduler 的调度线程拥有独立连接（路由数据源依赖 ThreadLocal，调度线程中不可用）。
 *
 * <p>解析顺序：
 * <ol>
 *     <li>{@code mcdull.scheduler.data-source-url}</li>
 *     <li>容器中名为 {@code dataSource} 的普通数据源</li>
 *     <li>基于 {@code spring.datasource.*} 自建连接池</li>
 *     <li>容器中唯一的数据源</li>
 * </ol>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
public class SchedulerDataSourceProvider implements AutoCloseable {

    private final DataSource dataSource;

    /**
     * 是否由本组件创建（需要在关闭时释放）
     */
    private final boolean owned;

    public SchedulerDataSourceProvider(SchedulerProperties properties,
                                       Environment environment,
                                       ApplicationContext applicationContext) {
        DataSourceHolder holder = resolve(properties, environment, applicationContext);
        this.dataSource = holder.dataSource;
        this.owned = holder.owned;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private DataSourceHolder resolve(SchedulerProperties properties,
                                     Environment environment,
                                     ApplicationContext applicationContext) {
        if (StringUtils.hasText(properties.getDataSourceUrl())) {
            log.info("[mcdull-scheduler] 使用指定的调度表库: {}", properties.getDataSourceUrl());
            DataSource ds = DataSourceBuilder.create()
                    .url(properties.getDataSourceUrl())
                    .username(properties.getDataSourceUsername())
                    .password(properties.getDataSourcePassword())
                    .driverClassName(properties.getDataSourceDriverClassName())
                    .build();
            return new DataSourceHolder(ds, true);
        }

        if (applicationContext.containsBean("dataSource")) {
            Object bean = applicationContext.getBean("dataSource");
            if (bean instanceof DataSource) {
                log.info("[mcdull-scheduler] 复用容器中的数据源 dataSource");
                return new DataSourceHolder((DataSource) bean, false);
            }
        }

        String url = environment.getProperty("spring.datasource.url");
        if (StringUtils.hasText(url)) {
            log.info("[mcdull-scheduler] 基于 spring.datasource 配置创建调度表库连接池: {}", url);
            DataSource ds = DataSourceBuilder.create()
                    .url(url)
                    .username(environment.getProperty("spring.datasource.username"))
                    .password(environment.getProperty("spring.datasource.password"))
                    .driverClassName(environment.getProperty("spring.datasource.driver-class-name"))
                    .build();
            return new DataSourceHolder(ds, true);
        }

        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
        if (dataSources.size() == 1) {
            String name = dataSources.keySet().iterator().next();
            log.info("[mcdull-scheduler] 使用容器中唯一的数据源: {}", name);
            return new DataSourceHolder(dataSources.values().iterator().next(), false);
        }

        throw new IllegalStateException("[mcdull-scheduler] 无法确定调度表库 DataSource，"
                + "请配置 mcdull.scheduler.data-source-url 或 spring.datasource.url");
    }

    @Override
    public void close() {
        if (!owned || dataSource == null) {
            return;
        }
        try {
            if (dataSource instanceof AutoCloseable closeable) {
                closeable.close();
            }
        } catch (Exception e) {
            log.warn("[mcdull-scheduler] 关闭调度表库连接池失败: {}", e.getMessage());
        }
    }

    private static final class DataSourceHolder {
        private final DataSource dataSource;
        private final boolean owned;

        private DataSourceHolder(DataSource dataSource, boolean owned) {
            this.dataSource = dataSource;
            this.owned = owned;
        }
    }
}
