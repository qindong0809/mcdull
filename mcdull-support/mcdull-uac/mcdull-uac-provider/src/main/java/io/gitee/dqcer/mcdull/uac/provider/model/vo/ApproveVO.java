package io.gitee.dqcer.mcdull.uac.provider.model.vo;

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

    private Boolean isSameDepartment;
}
