package io.gitee.dqcer.mcdull.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 初始或转移枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateInitialOrTransferEnum implements IEnum<Integer> {
    NONE(1, "无"),
    INITIAL(2, "初始"),
    TRANSFER(3, "转注"),
    OTHER(4, "其它"),
    ;
    CertificateInitialOrTransferEnum(Integer code, String message) {
        init(code, message);
    }
}
