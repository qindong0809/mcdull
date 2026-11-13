package io.gitee.dqcer.mcdull.focus.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * @author dqcer
 * @since 2026/03/27
 */
public enum TaskBatchBuildDateEnum implements IEnum<Integer> {
    // 1/今天 2/本周
    TODAY(1, "今天"),
    WEEK(2, "本周"),
    ;
    TaskBatchBuildDateEnum(Integer code, String text) {
        init(code, text);
    }
}
