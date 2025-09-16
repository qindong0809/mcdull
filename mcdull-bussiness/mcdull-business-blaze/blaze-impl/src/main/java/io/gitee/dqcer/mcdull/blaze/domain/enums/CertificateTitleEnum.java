package io.gitee.dqcer.mcdull.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 证书标题枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateTitleEnum implements IEnum<Integer> {

    NONE(1, "无"),
    PRIMARY(2, "初级"),
    MIDDLE(3, "中级"),
    HIGH(4, "高级"),
    OTHER(5, "不限");
    ;
    CertificateTitleEnum(Integer code, String message) {
        init(code, message);
    }
}
