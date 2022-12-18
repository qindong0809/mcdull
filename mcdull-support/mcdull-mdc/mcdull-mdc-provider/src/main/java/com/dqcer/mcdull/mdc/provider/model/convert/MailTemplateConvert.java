package com.dqcer.mcdull.mdc.provider.model.convert;

import com.dqcer.mcdull.mdc.provider.model.entity.SysMailTemplateDO;
import com.dqcer.mcdull.mdc.provider.model.vo.MailTemplateBaseVO;
import com.dqcer.mcdull.mdc.provider.model.vo.MailTemplateVO;

public class MailTemplateConvert {


    public static MailTemplateVO convertToMailTemplateVO(SysMailTemplateDO item){
        if (item == null){
            return null;
        }
        MailTemplateVO vo = new MailTemplateVO();
        vo.setId(item.getId());
        vo.setStatus(item.getStatus());
        vo.setModelCode(item.getModelCode());
        vo.setTemplateName(item.getTemplateName());
        vo.setTemplateType(item.getTemplateType());
        vo.setDescr(item.getDescr());
        vo.setSubject(item.getSubject());
        vo.setContent(item.getContent());
        return vo;
    }

    public static MailTemplateBaseVO convertToMailTemplateBaseVO(SysMailTemplateDO item){
        if (item == null){
            return null;
        }
        MailTemplateBaseVO vo = new MailTemplateBaseVO();
        vo.setId(item.getId());
        vo.setStatus(item.getStatus());
        vo.setModelCode(item.getModelCode());
        vo.setTemplateName(item.getTemplateName());
        vo.setTemplateType(item.getTemplateType());
        vo.setDescr(item.getDescr());
        return vo;
    }

    private MailTemplateConvert() {
    }
}
