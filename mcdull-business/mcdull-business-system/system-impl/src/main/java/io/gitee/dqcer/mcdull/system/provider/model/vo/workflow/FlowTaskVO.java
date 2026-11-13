package io.gitee.dqcer.mcdull.system.provider.model.vo.workflow;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 流任务 vo
 *
 * @author dqcer
 * @since 2025/04/17
 */
@Data
public class FlowTaskVO implements VO {

    /**
     * 计划审批人
     */
    private String approver;

    /**
     * 转办人
     */
    private String transferredBy;

    /**
     * 委派人
     */
    private String delegate;

    /**
     * 委派人
     */
    private String flowStatus;

    /**
     * 激活状态
     */
    private Integer activityStatus;
}
