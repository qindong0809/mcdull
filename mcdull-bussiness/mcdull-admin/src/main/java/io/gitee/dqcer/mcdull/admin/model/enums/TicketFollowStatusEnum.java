package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2022/07/26
 */
public enum TicketFollowStatusEnum implements IEnum<Integer> {

    EDIT(1, "可编辑"),
    PUBLISHED(2, "已发布"),
    PASSED(3, "已通过"),
    EXECUTED(4, "已执行"),

    ;

    TicketFollowStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
