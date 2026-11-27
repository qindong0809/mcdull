package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批场景发起请求。
 *
 * <p>字段与 {@link StartProcessDTO} 一致，但面向业务系统暴露“发起审批”语义，
 * 避免调用方理解流程实例等引擎概念。</p>
 *
 * @author dqcer
 * @since 2026-11-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StartApprovalDTO extends StartProcessDTO {

    private static final long serialVersionUID = 1L;
}
