package io.gitee.dqcer.mcdull.mdc.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.vo.VO;


/**
 * 邮件模板 视图对象
 *
 * @author dqcer
 * @version  2022/11/16
 */
public class MailTemplateBaseVO implements VO {

    private Long id;

    /**
     * 状态（1/正常 2/停用）
     */
    private Integer status;

    /**
     * 所属模块编码 如：user:register
     */
    private String modelCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型
     */
    private String templateType;

    /**
     * 备注说明
     */
    private String descr;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MailTemplateBaseVO{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", modelCode='").append(modelCode).append('\'');
        sb.append(", templateName='").append(templateName).append('\'');
        sb.append(", templateType='").append(templateType).append('\'');
        sb.append(", descr='").append(descr).append('\'');
        sb.append('}');
        return sb.toString();
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
}
