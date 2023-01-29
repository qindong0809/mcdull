package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 资源 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class MenuLiteDTO extends PagedDTO {

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
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String name;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 模块code 如sys:user:list
     */
    private String resCode;
    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 类型 (menu/菜单、button/按钮)
     */
    private String type;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MenuLiteDTO{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", name='").append(name).append('\'');
        sb.append(", parentId=").append(parentId);
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", sort=").append(sort);
        sb.append(", resCode='").append(resCode).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", component='").append(component).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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