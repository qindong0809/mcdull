package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.system.provider.model.dto.HelpDocQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.HelpDocEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.HelpDocMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IHelpDocRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Help doc repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocRepositoryImpl
        extends CrudRepository<HelpDocMapper, HelpDocEntity> implements IHelpDocRepository {


    @Override
    public List<HelpDocEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<HelpDocEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(HelpDocEntity::getId, idList);
        List<HelpDocEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<HelpDocEntity> selectPage(HelpDocQueryDTO param) {
        LambdaQueryWrapper<HelpDocEntity> lambda = new QueryWrapper<HelpDocEntity>().lambda();
        Integer helpDocCatalogId = param.getHelpDocCatalogId();
        if (ObjUtil.isNotNull(helpDocCatalogId)) {
            lambda.eq(HelpDocEntity::getHelpDocCatalogId, helpDocCatalogId);
        }
        String keywords = param.getKeywords();
        if (StrUtil.isNotBlank(keywords)) {
            lambda.like(HelpDocEntity::getTitle, keywords);
        }
        LocalDate startDate = param.getCreateTimeBegin();
        LocalDate endDate = param.getCreateTimeEnd();
        if (ObjUtil.isAllNotEmpty(startDate, endDate)) {
            lambda.between(RelEntity::getCreatedTime, startDate,
                    LocalDateTimeUtil.endOfDay(endDate.atStartOfDay()));
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public HelpDocEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public Integer insert(HelpDocEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }


    @Override
    public boolean exist(HelpDocEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<HelpDocEntity> selectList(Integer userId) {
        LambdaQueryWrapper<HelpDocEntity> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }

    @Override
    public List<HelpDocEntity> listByCatalogId(Integer helpDocCatalogId) {
        LambdaQueryWrapper<HelpDocEntity> query = Wrappers.lambdaQuery();
        query.eq(HelpDocEntity::getHelpDocCatalogId, helpDocCatalogId);
        return baseMapper.selectList(query);
    }


    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}