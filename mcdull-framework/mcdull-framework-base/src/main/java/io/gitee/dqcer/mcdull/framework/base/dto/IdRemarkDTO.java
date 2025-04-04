package io.gitee.dqcer.mcdull.framework.base.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import jakarta.validation.constraints.NotNull;

/**
 * @author dqcer
 * @since 2025/04/03
 */
public class IdRemarkDTO implements DTO {

    @NotNull
    private Integer id;

    private String remark;


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
