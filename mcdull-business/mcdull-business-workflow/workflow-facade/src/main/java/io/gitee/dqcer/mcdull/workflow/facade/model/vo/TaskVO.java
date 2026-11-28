package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 业务属性快照，由调用方按 key 解释，工作流只原样回传。
     * <p>
     * 典型内容：businessTitle、businessNo、applicantName、departmentName、amountText 等。
     */
    private Map<String, Object> attributes = new HashMap<>();

    @Builder.Default
    private List<String> assigneeIds = new ArrayList<>();

    private String createTime;

    private String updateTime;
}
