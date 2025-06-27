package io.gitee.dqcer.mcdull.workflow.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.warm.flow.orm.entity.FlowTask;

/**
 * 待办任务
 *
 * @author qin.dong
 * @since 2025/06/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowTaskVO extends FlowTask implements VO {

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
