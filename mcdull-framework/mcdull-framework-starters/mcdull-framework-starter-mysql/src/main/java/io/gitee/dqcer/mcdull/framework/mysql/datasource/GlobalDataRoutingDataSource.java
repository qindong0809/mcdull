package io.gitee.dqcer.mcdull.framework.mysql.datasource;

import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.RoutingDataSource;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.mysql.datasource.GlobalDataRoutingDataSource",
        description = "GlobalDataRoutingDataSource")
public class GlobalDataRoutingDataSource extends RoutingDataSource {


    public void switchDataSource() {
        String lookupKey = dataSourceProperties.getDefaultName();
        DynamicContextHolder.clear();
        super.loadDataSource(lookupKey, dataSourceProperties);
        DynamicContextHolder.push(lookupKey);
    }

    public void removeDataSource() {
        DynamicContextHolder.clear();
    }

    public <T> T get(Supplier<T> supplier) {
        try {
            this.switchDataSource();
            return supplier.get();
        } finally {
            this.removeDataSource();
        }
    }
}
