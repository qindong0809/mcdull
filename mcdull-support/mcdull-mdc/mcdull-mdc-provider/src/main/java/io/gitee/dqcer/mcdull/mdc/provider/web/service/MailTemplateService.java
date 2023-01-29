package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.MailTemplateConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.MailTemplateLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.SysMailTemplateDO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateBaseVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.MailTemplateDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件模板服务
 *
 * @author dqcer
 * @since 2022/12/26 21:12:84
 */
@Service
public class MailTemplateService {

    @Resource
    private MailTemplateDAO mailTemplateDAO;

    public Result<List<MailTemplateBaseVO>> listAll() {
        List<MailTemplateBaseVO> listVo = new ArrayList<>();
        LambdaQueryWrapper<SysMailTemplateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysMailTemplateDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<SysMailTemplateDO> entityList = mailTemplateDAO.selectList(wrapper);
        for (SysMailTemplateDO entity : entityList) {
            listVo.add(MailTemplateConvert.convertToMailTemplateBaseVO(entity));
        }
        return Result.ok(listVo);
    }
    public Result<MailTemplateVO> detail(MailTemplateLiteDTO dto) {
        SysMailTemplateDO entity = mailTemplateDAO.selectById(dto.getId());
        return Result.ok(MailTemplateConvert.convertToMailTemplateVO(entity));
    }
}
