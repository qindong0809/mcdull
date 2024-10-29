package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocRelationEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocRelationMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocRelationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Help doc relation repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocRelationRepositoryImpl
        extends CrudRepository<HelpDocRelationMapper, HelpDocRelationEntity> implements IHelpDocRelationRepository {

    @Override
    public List<HelpDocRelationEntity> listByRelationId(Integer relationId) {
        LambdaQueryWrapper<HelpDocRelationEntity> query = Wrappers.lambdaQuery();
        query.eq(HelpDocRelationEntity::getRelationId, relationId);
        return baseMapper.selectList(query);
    }
}