package io.gitee.dqcer.mcdull.system.facade.model.vo;

import lombok.Data;

@Data
public class ApproveVO {

    private Integer id;

    /**
     * 审批状态，取值参见 blaze 模块 ApproveEnum
     */
    private Integer approve;

    private String approveStr;

    private String approveRemarks;

    private Integer responsibleUserId;

    private String responsibleUserIdStr;

    private String responsibleUserDepartmentName;

    private Integer responsibleUserDepartmentId;

    private String responsibleUserPhone;

    private Boolean isSameDepartment;
}
