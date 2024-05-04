package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocRelationEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocCatalogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocRelationMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocCatalogRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocRelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class HelpDocRelationRepositoryImpl
        extends ServiceImpl<HelpDocRelationMapper, HelpDocRelationEntity>  implements IHelpDocRelationRepository {

    private static final Logger log = LoggerFactory.getLogger(HelpDocRelationRepositoryImpl.class);

    @Override
    public List<HelpDocRelationEntity> listByRelationId(Long relationId) {
        LambdaQueryWrapper<HelpDocRelationEntity> query = Wrappers.lambdaQuery();
        query.eq(HelpDocRelationEntity::getRelationId, relationId);
        return baseMapper.selectList(query);
    }
}