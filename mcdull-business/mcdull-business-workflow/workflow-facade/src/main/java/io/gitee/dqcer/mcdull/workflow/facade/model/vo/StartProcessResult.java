package io.gitee.dqcer.mcdull.workflow.facade.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发起流程结果。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartProcessResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long instanceId;

    private String flowCode;

    private String businessId;

    private String flowStatus;

    private String currentNodeCode;

    private String currentNodeName;

    private String createTime;
}
