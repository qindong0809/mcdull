package io.gitee.dqcer.mcdull.system.facade.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 * 审批人部门信息（远程回填用）
 */
@Data
public class ApproveDeptVO implements VO {

    /**
     * 是否与当前用户同根部门
     */
    private Boolean isSameDepartment;

    /**
     * 审批人所在根部门 id
     */
    private Integer responsibleUserDepartmentId;

    /**
     * 审批人所在根部门名称
     */
    private String responsibleUserDepartmentName;

    /**
     * 审批人手机号
     */
    private String responsibleUserPhone;
}
