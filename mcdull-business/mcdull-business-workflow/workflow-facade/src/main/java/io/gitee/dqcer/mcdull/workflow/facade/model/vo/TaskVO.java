package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务 VO。人员名称均为发起/办理时写入的快照。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    private Long instanceId;

    private Long definitionId;

    private String flowCode;

    private String flowStatus;

    private String nodeCode;

    private String nodeName;

    private String businessId;

    private String businessTitle;

    private String starterId;

    private String starterName;

    @Builder.Default
    private List<String> assigneeIds = new ArrayList<>();

    @Builder.Default
    private List<String> assigneeNames = new ArrayList<>();

    private String createTime;

    private String updateTime;

    private Long revision;
}
