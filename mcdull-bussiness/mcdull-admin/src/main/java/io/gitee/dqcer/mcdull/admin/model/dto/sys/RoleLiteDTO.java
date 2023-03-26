package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.StringJoiner;

/**
* 角色 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
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
     @EnumsStrValid(groups = {ValidGroup.Status.class}, value = StatusEnum.class)
     private String status;

    /**
     * 删除标识（1/正常 2/删除）
     */
    @EnumsIntValid(groups = {ValidGroup.Delete.class}, value = DelFlayEnum.class)
    private Integer delFlag;

    /**
     * 昵称
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String name;

    /**
     * 描述
     */
    @Length(groups = {ValidGroup.Insert.class},  max = 2048)
    private String description;

    @Override
    public String toString() {
        return new StringJoiner(", ", RoleLiteDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("status=" + status)
                .add("delFlag=" + delFlag)
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("pageSize=" + pageSize)
                .add("page=" + pageNum)
                .add("orders=" + orders)
                .add("keyword='" + keyword + "'")
                .toString();
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Integer getDelFlag() {
        return delFlag;
    }

    public RoleLiteDTO setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}