package io.gitee.dqcer.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 三类人员 枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateThreePersonnerEnum implements IEnum<Integer> {

    NONE(1, "无"),
    A_CERTIFICATE(2, "有A证"),
    B_CERTIFICATE(3, "有B证"),
    C_CERTIFICATE(4, "有C证"),
    CAN_EXAMINE_A_CERTIFICATE(5, "可考A证"),
    CAN_EXAMINE_B_CERTIFICATE(6, "可考B证"),
    CAN_EXAMINE_C_CERTIFICATE(7, "可考C证"),
    NOT_EXAMINE_A_CERTIFICATE(8, "不考A证"),
    NOT_EXAMINE_B_CERTIFICATE(9, "不考B证"),
    NOT_EXAMINE_C_CERTIFICATE(10, "不考C证"),
    OTHER(11, "其它")

    ;
    CertificateThreePersonnerEnum(Integer code, String message) {
        init(code, message);
    }
}
