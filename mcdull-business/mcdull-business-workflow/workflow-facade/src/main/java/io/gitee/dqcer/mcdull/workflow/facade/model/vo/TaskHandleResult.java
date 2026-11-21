package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 任务办理结果。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskHandleResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    private Long instanceId;

    private Long historyId;

    private String action;

    private String flowStatus;

    private String nextNodeCode;

    private String nextNodeName;

    private boolean finished;
}
