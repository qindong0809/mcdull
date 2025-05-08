package io.gitee.dqcer.mcdull.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 社保要求
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateSocialSecurityRequirementEnum implements IEnum<Integer> {
    NONE(1, "无社保"),
    ONLY_TRANSFER(2, "唯一社保可转"),
    ONLY_STOP(3, "唯一社保可停"),
    PUBLIC_SECURITY_NOT_UNIQUE(4, "国企社保非唯一"),
    PRIVATE_SECURITY_NOT_UNIQUE(5, "私企社保非唯一"),
    PRIVATE_SECURITY_NOT_UNIQUE(5, "退休"),
    ;
    CertificateSocialSecurityRequirementEnum(Integer code, String message) {
        init(code, message);
    }
}
