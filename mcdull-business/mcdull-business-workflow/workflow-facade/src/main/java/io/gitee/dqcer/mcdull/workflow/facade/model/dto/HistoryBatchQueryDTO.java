package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 按实例 ID 批量查询审批历史。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class HistoryBatchQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "instanceIds 不能为空")
    private List<Long> instanceIds = new ArrayList<>();

    /**
     * 每个实例最多返回多少条，默认 50，最大 200。
     */
    private Integer limitPerInstance = 50;
}
