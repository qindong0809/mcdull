package io.gitee.dqcer.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 证书招标出场枚举
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum CertificateBiddingExitEnum implements IEnum<Integer> {
    CAN_EXIT_NOT_BIDDING(1, "可出场不招标"),
    NOT_EXIT_CAN_BIDDING(2, "不出场可招标"),
    EXIT_BIDDING(3, "出场招标"),
    PROJECT(4, "项目"),
    QUALIFICATION(5, "资质"),
    OTHER(6, "其它情况");
    ;
    CertificateBiddingExitEnum(Integer code, String message) {
        init(code, message);
    }
}
