package com.dqcer.mcdull.uac.api.dto;

import com.dqcer.framework.base.dto.PagedDTO;
import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.annotation.EnumsIntValid;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @version 2022-11-16
*/
public class RoleLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class, ValidGroup.Delete.class})
     private Long id;
    /**
     * 状态（1/正常 2/停用）
     */
     @EnumsIntValid(groups = {ValidGroup.Status.class}, value = StatusEnum.class)
     private Integer status;

    /**
     * 删除标识（1/正常 2/删除）
     */
    @EnumsIntValid(groups = {ValidGroup.Delete.class}, value = DelFlayEnum.class)
    private Integer delFlag;

    /**
     * 昵称
     */
    @NotBlank(groups = {ValidGroup.Add.class})
    @Length(groups = {ValidGroup.Add.class}, min = 1, max = 512)
    private String name;

    /**
     * 描述
     */
    @Length(groups = {ValidGroup.Add.class},  max = 2048)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}