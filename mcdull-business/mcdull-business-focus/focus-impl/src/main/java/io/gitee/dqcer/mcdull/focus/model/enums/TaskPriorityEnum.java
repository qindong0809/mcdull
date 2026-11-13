package io.gitee.dqcer.mcdull.focus.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 任务优先级枚举
 *
 * @author dqcer
 * @since 2026/03/27
 */
public enum TaskPriorityEnum implements IEnum<Integer> {
    // 0=低 1=中 2=高 3=紧急
    LOW(0, "低"),
    MIDDLE(1, "中"),
    HIGH(2, "高"),
    EMERGENCY(3, "紧急"),
    ;
    TaskPriorityEnum(Integer code, String text) {
        init(code, text);
    }
}
