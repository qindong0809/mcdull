package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 审批历史 VO。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long historyId;

    private Long instanceId;

    private String nodeCode;

    private String nodeName;

    private String handlerId;

    /**
     * 业务属性快照，由调用方按 key 解释，工作流只原样回传。
     * <p>
     * 典型内容：approverName、approverDepartmentName、commentText 等。
     */
    private Map<String, Object> attributes = new HashMap<>();

    private String action;

    private String skipType;

    private String comment;

    private String createTime;

    private String updateTime;
}
