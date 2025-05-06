package io.gitee.dqcer.mcdull.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 证书状态枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateStatusEnum implements IEnum<Integer> {

    NORMAL(1, "正常"),
    ABNORMAL(2, "不正常")
    ;
    CertificateStatusEnum(Integer code, String message) {
        init(code, message);
    }
}
