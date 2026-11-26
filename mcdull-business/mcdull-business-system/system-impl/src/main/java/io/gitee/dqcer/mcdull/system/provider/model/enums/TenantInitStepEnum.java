package io.gitee.dqcer.mcdull.system.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

public enum TenantInitStepEnum implements IEnum<String> {
    DB_EXISTS_CHECK("DB_EXISTS_CHECK", "预检库名"),
    CREATE_DATABASE("CREATE_DATABASE", "创建数据库"),
    APP_SCRIPT("APP_SCRIPT", "执行应用脚本"),
    WRITE_BACK("WRITE_BACK", "回写状态"),
    DONE("DONE", "完成");

    TenantInitStepEnum(String value, String desc) {
        this.init(value, desc);
    }
}
