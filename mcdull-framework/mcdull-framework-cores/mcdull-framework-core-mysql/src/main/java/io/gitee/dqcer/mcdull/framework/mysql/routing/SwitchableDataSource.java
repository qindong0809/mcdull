package io.gitee.dqcer.mcdull.framework.mysql.routing;

import io.gitee.dqcer.mcdull.framework.spi.RequestDataRouting;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * 可切换的数据源接口。
 *
 * @author dqcer
 * @since 2024/12/19
 */
public interface SwitchableDataSource extends DataSource, RequestDataRouting {

    /**
     * 切换数据源
     */
    void switchDataSource();

    /**
     * 移除数据源
     */
    void removeDataSource();

    /**
     * 在数据源上下文中执行指定操作
     *
     * @param supplier 操作
     * @param <T> 返回类型
     * @return 操作结果
     */
    <T> T get(Supplier<T> supplier);

    /** 指定 lookupKey 的数据源是否已注册 */
    boolean containsDataSource(String lookupKey);

    /** 已注册数据源数量（含默认数据源） */
    int dataSourceCount();

    /**
     * 注册一个外部构造的数据源。
     * <p>
     * 参数用 JDK 的 {@link DataSource} 而非 starter 层的 Database，
     * 避免 core-mysql 反向依赖 starter-mysql。调用方负责构造连接池。
     */
    void addDataSource(String lookupKey, DataSource dataSource);
}
