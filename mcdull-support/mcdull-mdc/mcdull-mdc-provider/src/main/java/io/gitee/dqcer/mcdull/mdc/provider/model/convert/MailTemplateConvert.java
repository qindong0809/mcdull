package io.gitee.dqcer.mcdull.mdc.provider.model.convert;

import io.gitee.dqcer.mcdull.mdc.provider.model.entity.MailTemplateEntity;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateBaseVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateVO;

/**
 * 邮件模板转换
 *
 * @author dqcer
 * @since 2022/12/26 21:12:88
 */
public class MailTemplateConvert {


    public static MailTemplateVO convertToMailTemplateVO(MailTemplateEntity item){
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

    public static MailTemplateBaseVO convertToMailTemplateBaseVO(MailTemplateEntity item){
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
