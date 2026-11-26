package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

public enum TenantOwnerTypeEnum implements IEnum<Integer> {
    ENTERPRISE(1, "企业"),
    TEAM(2, "团队"),
    PERSON(3, "个人"),
    TRIAL(4, "试用"),
    INTERNAL(5, "内部");

    TenantOwnerTypeEnum(Integer value, String desc) {
        this.init(value, desc);
    }
}
