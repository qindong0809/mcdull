package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 流程定义 VO。字段来自工作流自身表，可安全缓存。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefinitionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String definitionKey;

    private Long definitionId;

    private String flowCode;

    private String flowName;

    private String category;

    private String version;

    private Integer publishStatus;

    private Integer activityStatus;

    private String formCustom;

    private String formPath;

    private String ext;

    private String updateTime;
}
