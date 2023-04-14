package io.gitee.dqcer.mcdull.admin.web.manager.common;


import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;

/**
 * 用户 通用逻辑定义
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface ISysConfigManager {

    /**
     * 通过枚举找到value
     *
     * @param keyEnum 实体
     * @return {@link String}
     */
    String findValueByEnum(SysConfigKeyEnum keyEnum);
}
