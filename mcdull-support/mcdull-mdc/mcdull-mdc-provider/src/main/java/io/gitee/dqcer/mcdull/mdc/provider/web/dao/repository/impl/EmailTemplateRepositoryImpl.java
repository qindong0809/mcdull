package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.EmailTemplateEntity;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.EmailTemplateMapper;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.IEmailTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
@Service
public class EmailTemplateRepositoryImpl extends ServiceImpl<EmailTemplateMapper, EmailTemplateEntity>
        implements IEmailTemplateRepository {

    @Override
    public Map<String, EmailTemplateEntity> map(List<String> codeList) {
        if (CollUtil.isNotEmpty(codeList)) {
            LambdaQueryWrapper<EmailTemplateEntity> query = Wrappers.lambdaQuery();
            query.in(EmailTemplateEntity::getCode, codeList);
            List<EmailTemplateEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.toMap(EmailTemplateEntity::getCode, Function.identity()));
            }
        }
        return Collections.emptyMap();
    }
}
