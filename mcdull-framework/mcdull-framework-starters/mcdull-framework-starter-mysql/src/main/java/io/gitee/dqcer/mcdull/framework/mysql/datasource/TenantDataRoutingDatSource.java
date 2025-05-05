package io.gitee.dqcer.mcdull.framework.mysql.datasource;

import io.gitee.dqcer.mcdull.framework.mysql.Database;
import io.gitee.dqcer.mcdull.framework.mysql.config.DynamicContextHolder;
import io.gitee.dqcer.mcdull.framework.mysql.config.RoutingDataSource;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.function.Supplier;


//@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.mysql.datasource.TenantDataRoutingDatSource",
        description = "TenantDataRoutingDatSource")
public class TenantDataRoutingDatSource extends RoutingDataSource {


    public void switchDataSource(String tenantId) {
        String lookupKey = tenantId;
        DynamicContextHolder.clear();
        Database tenantDatabase = null;
        // todo Database tenantDatabase = feign.getTenantDatabase(tenantId);
        super.loadDataSource(lookupKey, tenantDatabase);
        DynamicContextHolder.push(lookupKey);
    }

    public void removeDataSource() {
        DynamicContextHolder.clear();
    }

    public <T> T get(Supplier<T> supplier, String tenantId) {
        try {
            this.switchDataSource(tenantId);
            return supplier.get();
        } finally {
            this.removeDataSource();
        }
    }
}
