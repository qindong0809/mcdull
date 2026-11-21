package io.gitee.dqcer.mcdull.workflow.facade.model.dto;

import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 按业务键批量查询。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
public class BusinessKeyBatchQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantId;

    @Valid
    @NotEmpty(message = "keys 不能为空")
    private List<ProcessBusinessKey> keys = new ArrayList<>();
}
