package io.gitee.dqcer.mcdull.focus.model.enums;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * @author dqcer
 * @since 2026/03/27
 */
public enum FocusModeEnum implements IEnum<Integer> {
    // 0=正计时 1=番茄钟 2=倒计时
    TIMER(0, "正计时"),
    FOCUS(1, "番茄钟"),
    COUNTDOWN(2, "倒计时"),
    ;
    FocusModeEnum(Integer code, String text) {
        init(code, text);
    }
}
