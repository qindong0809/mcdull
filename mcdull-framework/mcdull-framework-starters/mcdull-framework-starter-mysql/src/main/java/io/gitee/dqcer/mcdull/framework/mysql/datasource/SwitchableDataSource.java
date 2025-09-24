package io.gitee.dqcer.mcdull.framework.mysql.datasource;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * 可切换的数据源接口
 *
 * @author dqcer
 * @since 2024/12/19
 */
public interface SwitchableDataSource extends DataSource {

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
}
