package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocRelationEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.HelpDocRelationMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IHelpDocRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HelpDocRelationServiceImpl extends BasicCurdServiceImpl<HelpDocRelationMapper, HelpDocRelationEntity> implements IHelpDocRelationService {

    @Override
    public List<HelpDocRelationEntity> listByRelationId(Integer relationId) {
        LambdaQueryWrapper<HelpDocRelationEntity> query = Wrappers.lambdaQuery();
        query.eq(HelpDocRelationEntity::getRelationId, relationId);
        return baseMapper.selectList(query);
    }
}
