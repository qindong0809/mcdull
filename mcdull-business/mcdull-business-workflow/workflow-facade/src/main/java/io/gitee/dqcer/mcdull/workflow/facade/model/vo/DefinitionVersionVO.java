package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 定义版本信息，供调用方做本地缓存失效判断。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionVersionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String definitionKey;

    private String version;

    private String updateTime;

    private Integer publishStatus;

    private Integer activityStatus;
}
