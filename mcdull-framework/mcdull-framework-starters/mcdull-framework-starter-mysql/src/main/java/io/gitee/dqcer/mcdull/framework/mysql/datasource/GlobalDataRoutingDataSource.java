package io.gitee.dqcer.mcdull.framework.mysql.datasource;

import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.RoutingDataSource;
import io.gitee.dqcer.mcdull.framework.mysql.routing.SwitchableDataSource;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.mysql.datasource.GlobalDataRoutingDataSource",
        description = "GlobalDataRoutingDataSource")
public class GlobalDataRoutingDataSource extends RoutingDataSource implements SwitchableDataSource {


    @Override
    public void switchDataSource() {
        String lookupKey = dataSourceProperties.getDefaultName();
        // 不再 clear()：请求边界的清理由 HttpTraceLogFilter 的 finally 负责。
        super.loadDataSource(lookupKey, dataSourceProperties);
        DynamicContextHolder.push(lookupKey);
    }

    @Override
    public void removeDataSource() {
        DynamicContextHolder.clear();
    }

    @Override
    public <T> T get(Supplier<T> supplier) {
        try {
            this.switchDataSource();
            return supplier.get();
        } finally {
            this.removeDataSource();
        }
    }
}
