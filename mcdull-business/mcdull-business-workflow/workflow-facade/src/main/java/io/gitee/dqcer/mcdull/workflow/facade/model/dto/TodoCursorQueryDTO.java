package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 待办游标分页请求。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class TodoCursorQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    @NotBlank(message = "assigneeId 不能为空")
    private String assigneeId;

    /**
     * 格式：createTime|id。首页不传。
     */
    private String cursor;

    @Min(value = 1, message = "pageSize 最小为 1")
    @Max(value = 200, message = "pageSize 最大为 200")
    private Integer pageSize = 50;

    private String keyword;

    private String flowCode;

    /**
     * 为 false 时不执行 count(*)。
     */
    private boolean needTotal;
}
