package io.gitee.dqcer.mcdull.blaze.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.blaze.dao.mapper.TalentMapper;
import io.gitee.dqcer.mcdull.blaze.dao.repository.ITalentRepository;
import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentQueryDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


/**
 * 人才表 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */
@Service
public class TalentRepositoryImpl extends
        ServiceImpl<TalentMapper, TalentEntity> implements ITalentRepository {

    @Override
    public List<TalentEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<TalentEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TalentEntity::getId, idList);
        List<TalentEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        wrapper.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return Collections.emptyList();
    }


    @Override
    public Page<TalentEntity> selectPage(TalentQueryDTO param) {
        LambdaQueryWrapper<TalentEntity> lambda = Wrappers.lambdaQuery();
        lambda.in(CollUtil.isNotEmpty(param.getResponsibleUserIdList()), TalentEntity::getResponsibleUserId, param.getResponsibleUserIdList());
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

}