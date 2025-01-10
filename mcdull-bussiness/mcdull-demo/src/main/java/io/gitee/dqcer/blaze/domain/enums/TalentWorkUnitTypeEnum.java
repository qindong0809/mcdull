package io.gitee.dqcer.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 证书标题枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum TalentWorkUnitTypeEnum implements IEnum<Integer> {
    PRIVATE_ENTERPRISE(1, "私企"),
    PUBLIC_ENTERPRISE(2, "国企"),
    OTHER(3, "其它"),
    ;
    TalentWorkUnitTypeEnum(Integer code, String message) {
        init(code, message);
    }
}
