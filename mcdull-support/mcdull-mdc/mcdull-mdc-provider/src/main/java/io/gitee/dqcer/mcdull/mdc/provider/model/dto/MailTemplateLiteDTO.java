package io.gitee.dqcer.mcdull.mdc.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.annotation.EnumsStrValid;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* 邮件模板 接收客户端参数
*
* @author dqcer
* @since 2022-11-16
*/
public class MailTemplateLiteDTO extends PagedDTO {

    private static final long serialVersionUID = 1L;

     /**
      * 主键
      */
     @NotNull(groups = {ValidGroup.Update.class, ValidGroup.One.class, ValidGroup.Status.class})
     private Long id;
    /**
     * 状态（1/正常 2/停用）
     */
     @NotNull(groups = {ValidGroup.Status.class})
     private Integer status;
    /**
     * 删除标识（1/正常 2/删除）
     */
    @NotNull(groups = {ValidGroup.Insert.class})
    @EnumsStrValid(groups = {ValidGroup.Insert.class}, value = StatusEnum.class)
    private Integer delFlag;

    /**
     * 所属模块编码 如：user:register
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String modelCode;

    /**
     * 模板名称
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String templateName;

    /**
     * 模板类型
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String templateType;

    /**
     * 备注说明
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String descr;

    /**
     * 标题（包含占位符）
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String subject;

    /**
     * 正文（包含占位符）
     */
    @NotBlank(groups = {ValidGroup.Insert.class})
    @Length(groups = {ValidGroup.Insert.class}, min = 1, max = 512)
    private String content;


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
    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
    return "SysMailTemplate{" +
            "id=" + id +
            ", status=" + status +
            ", delFlag=" + delFlag +
            ", modelCode=" + modelCode +
            ", templateName=" + templateName +
            ", templateType=" + templateType +
            ", descr=" + descr +
            ", subject=" + subject +
            ", content=" + content +
    "}";
    }
}