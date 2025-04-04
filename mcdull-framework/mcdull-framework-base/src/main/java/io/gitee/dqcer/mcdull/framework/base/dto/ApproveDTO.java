package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2025/04/03
 */
public class ApproveDTO implements DTO {

    @NotNull
    private Integer id;

    @NotNull
    private Integer approve;

    private String remark;

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
