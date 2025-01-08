package io.gitee.dqcer.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 职位来源枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificatePositionSourceEnum implements IEnum<Integer> {
    COMPANY_DIRECT_SIGN(1, "企业直签"),
    COOPERATIVE_MEDIATOR(2, "同行中介"),
    ;
    CertificatePositionSourceEnum(Integer code, String message) {
        init(code, message);
    }
}
