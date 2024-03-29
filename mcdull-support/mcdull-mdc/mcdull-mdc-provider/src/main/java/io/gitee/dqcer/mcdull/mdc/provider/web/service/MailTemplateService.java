package io.gitee.dqcer.mcdull.mdc.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.provider.model.convert.MailTemplateConvert;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.MailTemplateLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.MailTemplateEntity;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateBaseVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.MailTemplateVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.MailTemplateMapper;
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
    private MailTemplateMapper mailTemplateMapper;

    public Result<List<MailTemplateBaseVO>> listAll() {
        List<MailTemplateBaseVO> listVo = new ArrayList<>();
        LambdaQueryWrapper<MailTemplateEntity> wrapper = Wrappers.lambdaQuery();
        List<MailTemplateEntity> entityList = mailTemplateMapper.selectList(wrapper);
        for (MailTemplateEntity entity : entityList) {
            listVo.add(MailTemplateConvert.convertToMailTemplateBaseVO(entity));
        }
        return Result.success(listVo);
    }
    public Result<MailTemplateVO> detail(MailTemplateLiteDTO dto) {
        MailTemplateEntity entity = mailTemplateMapper.selectById(dto.getId());
        return Result.success(MailTemplateConvert.convertToMailTemplateVO(entity));
    }
}
