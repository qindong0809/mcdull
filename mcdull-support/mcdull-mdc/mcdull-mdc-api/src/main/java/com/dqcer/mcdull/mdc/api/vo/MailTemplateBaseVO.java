package com.dqcer.mcdull.mdc.api.vo;

import com.dqcer.framework.base.VO;


/**
 * 邮件模板 视图对象
 *
 * @author dqcer
 * @version  2022/11/16
 */
public class MailTemplateBaseVO extends VO {

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
