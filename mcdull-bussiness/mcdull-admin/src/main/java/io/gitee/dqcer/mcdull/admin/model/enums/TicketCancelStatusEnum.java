package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2022/07/26
 */
public enum TicketCancelStatusEnum implements IEnum<Integer> {

    /**
     * 回退状态（1正常 2dev:撤回 3test:不通过 4prod:驳回）
     */

    OK(1, "1正常"),
    CANCEL(2, "撤回"),
    NO_PASSED(3, "不通过"),
    REJECTED(4, "驳回"),

    ;

    TicketCancelStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
