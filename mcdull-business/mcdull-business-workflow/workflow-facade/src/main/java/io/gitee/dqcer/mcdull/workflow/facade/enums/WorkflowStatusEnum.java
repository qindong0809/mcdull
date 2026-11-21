package io.gitee.dqcer.mcdull.workflow.facade.enums;

/**
 * 流程实例 / 任务状态枚举，与 flow_status 字段保持一致。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public enum WorkflowStatusEnum {

    DRAFT("0", "待提交"),
    RUNNING("1", "审批中"),
    APPROVED("2", "审批通过"),
    TERMINATED("4", "终止"),
    INVALID("5", "作废"),
    WITHDRAWN("6", "撤销"),
    COMPLETED("8", "已完成"),
    REJECTED("9", "已退回"),
    EXPIRED("10", "失效"),
    TAKEN_BACK("11", "拿回");

    private final String code;
    private final String text;

    WorkflowStatusEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static String textOf(String code) {
        for (WorkflowStatusEnum e : values()) {
            if (e.code.equals(code)) {
                return e.text;
            }
        }
        return "";
    }

    public static boolean isFinal(String code) {
        return APPROVED.code.equals(code)
                || COMPLETED.code.equals(code)
                || TERMINATED.code.equals(code)
                || INVALID.code.equals(code)
                || WITHDRAWN.code.equals(code)
                || REJECTED.code.equals(code)
                || EXPIRED.code.equals(code);
    }
}
