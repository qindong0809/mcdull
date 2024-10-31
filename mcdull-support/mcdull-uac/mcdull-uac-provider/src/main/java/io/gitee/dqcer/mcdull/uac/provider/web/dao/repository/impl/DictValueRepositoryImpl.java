package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictValueUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictValueEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.DictValueMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictValueRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
public class DictValueRepositoryImpl
        extends CrudRepository<DictValueMapper, DictValueEntity> implements IDictValueRepository {

    @Override
    public void insert(DictValueEntity entity) {
        baseMapper.insert(entity);
    }


    @Override
    public List<DictValueEntity> getListByDictKeyId(Integer dictKeyId) {
        if (ObjUtil.isNotNull(dictKeyId)) {
            LambdaQueryWrapper<DictValueEntity> wrapper = Wrappers.lambdaQuery(DictValueEntity.class)
                    .eq(DictValueEntity::getDictKeyId, dictKeyId);
            return baseMapper.selectList(wrapper);
        }
        return Collections.emptyList();
    }

    @Override
    public Page<DictValueEntity> selectPage(DictValueQueryDTO dto) {
        LambdaQueryWrapper<DictValueEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getSearchWord();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictValueEntity::getValueName, keyword)
                    .or().like(DictValueEntity::getValueCode, keyword)
            );
        }
        Integer dictKeyId = dto.getDictKeyId();
        if (ObjUtil.isNotNull(dictKeyId)) {
            query.eq(DictValueEntity::getDictKeyId, dictKeyId);
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public DictValueEntity insert(DictValueAddDTO dto) {
        DictValueEntity entity = new DictValueEntity();
        entity.setDictKeyId(dto.getDictKeyId());
        entity.setValueCode(dto.getValueCode());
        entity.setValueName(dto.getValueName());
        entity.setSort(dto.getSort());
        entity.setRemark(dto.getRemark());
        this.insert(entity);
        return entity;
    }

    @Override
    public void update(DictValueUpdateDTO dto) {
        DictValueEntity entity = new DictValueEntity();
        entity.setId(dto.getDictValueId());
        entity.setValueCode(dto.getValueCode());
        entity.setValueName(dto.getValueName());
        entity.setSort(dto.getSort());
        entity.setRemark(dto.getRemark());
        this.updateById(entity);
    }
}
