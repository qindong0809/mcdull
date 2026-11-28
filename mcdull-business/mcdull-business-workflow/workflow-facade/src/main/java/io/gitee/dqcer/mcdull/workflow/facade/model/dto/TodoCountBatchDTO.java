package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量待办统计请求。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class TodoCountBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "assigneeIds 不能为空")
    private List<String> assigneeIds = new ArrayList<>();

    private List<String> flowCodes = new ArrayList<>();

    /**
     * 支持 FLOW_CODE；为空时只按 assigneeId 汇总。
     */
    private String groupBy;
}
