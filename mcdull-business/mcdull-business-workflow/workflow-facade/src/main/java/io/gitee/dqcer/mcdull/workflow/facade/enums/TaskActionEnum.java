package io.gitee.dqcer.mcdull.workflow.facade.enums;

import java.util.Arrays;

/**
 * 审批动作枚举。契约只传 code，不传中文文案。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public enum TaskActionEnum {

    APPROVE("APPROVE", "同意"),
    REJECT("REJECT", "驳回"),
    TRANSFER("TRANSFER", "转办"),
    DELEGATE("DELEGATE", "委派"),
    COUNTERSIGN("COUNTERSIGN", "加签"),
    WITHDRAW("WITHDRAW", "撤回"),
    TAKE_BACK("TAKE_BACK", "拿回"),
    URGE("URGE", "催办");

    private final String code;
    private final String description;

    TaskActionEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TaskActionEnum of(String code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equalsIgnoreCase(String.valueOf(code)))
                .findFirst()
                .orElse(null);
    }
}
