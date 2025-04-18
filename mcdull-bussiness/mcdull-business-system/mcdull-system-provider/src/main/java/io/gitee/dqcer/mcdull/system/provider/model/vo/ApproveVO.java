package io.gitee.dqcer.mcdull.system.provider.model.vo;

import lombok.Data;

@Data
public class ApproveVO {

    private Integer id;

    /**
     * {@link io.gitee.dqcer.blaze.domain.enums.ApproveEnum}
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
