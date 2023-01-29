package io.gitee.dqcer.mcdull.admin.model.dto.sys;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsIntValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 通知公告表 接收客户端参数
*
* @author dqcer
* @since 2023-01-18
*/
public class NoticeLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private Long id;
    /**
     * 公告标题
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String title;

    /**
     * 公告内容
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String content;

    /**
     * 公告类型（1通知 2公告）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsIntValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer type;

    /**
     * 状态（1/正常 2/停用）
     */
     @NotNull(groups = {ValidGroup.Status.class})
     private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
    return "SysNotice{" +
            "id=" + id +
            ", title=" + title +
            ", content=" + content +
            ", type=" + type +
            ", status=" + status +
    "}";
    }
}