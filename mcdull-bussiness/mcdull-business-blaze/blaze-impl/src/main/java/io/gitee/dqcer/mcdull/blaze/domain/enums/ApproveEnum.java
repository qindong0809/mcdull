package io.gitee.dqcer.mcdull.blaze.domain.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2025/01/08
 */
public enum ApproveEnum implements IEnum<Integer> {
    NOT_APPROVE(1, "待审批"),
    APPROVE(2, "已审批"),
    REJECT(3, "已驳回"),
    ;
    ApproveEnum(Integer code, String message) {
        init(code, message);
    }
}
