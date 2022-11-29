package com.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.convert.MailTemplateConvert;
import com.dqcer.mcdull.mdc.api.dto.MailTemplateLiteDTO;
import com.dqcer.mcdull.mdc.api.entity.SysMailTemplateEntity;
import com.dqcer.mcdull.mdc.api.vo.MailTemplateBaseVO;
import com.dqcer.mcdull.mdc.api.vo.MailTemplateVO;
import com.dqcer.mcdull.mdc.provider.web.dao.mapper.MailTemplateDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailTemplateService {

    @Resource
    private MailTemplateDAO mailTemplateDAO;

    public Result<List<MailTemplateBaseVO>> listAll() {
        List<MailTemplateBaseVO> listVo = new ArrayList<>();
        LambdaQueryWrapper<SysMailTemplateEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysMailTemplateEntity::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<SysMailTemplateEntity> entityList = mailTemplateDAO.selectList(wrapper);
        for (SysMailTemplateEntity entity : entityList) {
            listVo.add(MailTemplateConvert.convertToMailTemplateBaseVO(entity));
        }
        return Result.ok(listVo);
    }
    public Result<MailTemplateVO> detail(MailTemplateLiteDTO dto) {
        SysMailTemplateEntity entity = mailTemplateDAO.selectById(dto.getId());
        return Result.ok(MailTemplateConvert.convertToMailTemplateVO(entity));
    }
}
