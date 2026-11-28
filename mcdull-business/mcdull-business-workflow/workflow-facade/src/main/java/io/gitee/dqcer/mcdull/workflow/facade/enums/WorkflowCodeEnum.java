package io.gitee.dqcer.mcdull.workflow.facade.enums;

import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

/**
 * workflow 服务间错误码，独立占用 999600 - 999699 段。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public enum WorkflowCodeEnum implements ICode {

    DEFINITION_NOT_FOUND(999600, "流程定义不存在"),
    DEFINITION_NOT_PUBLISHED(999601, "流程定义未发布"),
    DEFINITION_DISABLED(999602, "流程定义已挂起"),
    INSTANCE_NOT_FOUND(999610, "流程实例不存在"),
    INSTANCE_STATUS_INVALID(999611, "流程实例状态不允许当前操作"),
    TASK_NOT_FOUND(999620, "流程任务不存在"),
    TASK_NOT_ASSIGNED(999621, "任务不属于当前办理人"),
    TASK_ACTION_INVALID(999622, "支持的审批动作无效"),
    BATCH_SIZE_EXCEEDED(999630, "批量条数超过上限"),
    CONCURRENT_CONFLICT(999633, "流程状态已被其他操作更新，请重试"),
    BUSINESS_KEY_CONFLICT(999634, "业务键已存在运行中的流程实例"),
    PERMISSION_DENIED(999640, "无权限调用工作流能力"),
    ENGINE_ERROR(999650, "工作流引擎处理失败");

    private final int code;
    private final String message;

    WorkflowCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
