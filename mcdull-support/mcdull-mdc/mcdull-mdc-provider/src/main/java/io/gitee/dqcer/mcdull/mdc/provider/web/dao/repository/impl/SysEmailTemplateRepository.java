package io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.DictDO;
import io.gitee.dqcer.mcdull.mdc.provider.model.entity.SysEmailTemplateDO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.mapper.SysEmailTemplateDAO;
import io.gitee.dqcer.mcdull.mdc.provider.web.dao.repository.ISysEmailTemplateRepository;
import org.apache.commons.math3.analysis.function.Cos;
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
public class SysEmailTemplateRepository extends ServiceImpl<SysEmailTemplateDAO, SysEmailTemplateDO>
        implements ISysEmailTemplateRepository {

    @Override
    public Map<String, SysEmailTemplateDO> map(List<String> codeList) {
        if (CollUtil.isNotEmpty(codeList)) {
            LambdaQueryWrapper<SysEmailTemplateDO> query = Wrappers.lambdaQuery();
            query.in(SysEmailTemplateDO::getCode, codeList);
            List<SysEmailTemplateDO> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.toMap(SysEmailTemplateDO::getCode, Function.identity()));
            }
        }
        return Collections.emptyMap();
    }
}
