package io.gitee.dqcer.mcdull.focus.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 任务优先级枚举
 *
 * @author dqcer
 * @since 2026/03/27
 */
public enum TaskCategoryEnum implements IEnum<String> {
    // "工作" / "学习" / "生活" / "其他"
    WORK("工作", ""),
    STUDY("学习", ""),
    LIFE("生活", ""),
    OTHER("其他", ""),
    ;
    TaskCategoryEnum(String code, String text) {
        init(code, text);
    }
}
