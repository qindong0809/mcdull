package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.HelpDocCatalogEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.HelpDocCatalogMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IHelpDocCatalogRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Help doc catalog repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class HelpDocCatalogRepositoryImpl
        extends CrudRepository<HelpDocCatalogMapper, HelpDocCatalogEntity> implements IHelpDocCatalogRepository {

    @Override
    public List<HelpDocCatalogEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<HelpDocCatalogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(HelpDocCatalogEntity::getId, idList);
        List<HelpDocCatalogEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }


    @Override
    public Page<HelpDocCatalogEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<HelpDocCatalogEntity> lambda = new QueryWrapper<HelpDocCatalogEntity>().lambda();
        String keyword = param.getKeyword();
        if (ObjUtil.isNotNull(keyword)) {
            lambda.like(HelpDocCatalogEntity::getName, keyword);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }


    @Override
    public HelpDocCatalogEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    public Integer insert(HelpDocCatalogEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean exist(HelpDocCatalogEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public List<HelpDocCatalogEntity> selectList(Integer userId) {
        LambdaQueryWrapper<HelpDocCatalogEntity> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }

    @Override
    public List<HelpDocCatalogEntity> list(Integer parentId) {
        if (ObjUtil.isNotNull(parentId)) {
            LambdaQueryWrapper<HelpDocCatalogEntity> query = Wrappers.lambdaQuery();
            query.eq(HelpDocCatalogEntity::getParentId, parentId);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }

    /**
    * 根据id删除批处理
    *
    * @param ids id集
    */
    @Override
    public void deleteBatchByIds(List<Integer> ids) {
        baseMapper.deleteByIds(ids);
    }
}