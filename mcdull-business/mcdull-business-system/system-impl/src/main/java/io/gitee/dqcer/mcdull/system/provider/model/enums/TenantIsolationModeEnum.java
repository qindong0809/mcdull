package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

public enum TenantIsolationModeEnum implements IEnum<Integer> {
    DATABASE(1, "独立库"),
    ROW(2, "共享库-行隔离（预留）");

    TenantIsolationModeEnum(Integer value, String desc) {
        this.init(value, desc);
    }
}
