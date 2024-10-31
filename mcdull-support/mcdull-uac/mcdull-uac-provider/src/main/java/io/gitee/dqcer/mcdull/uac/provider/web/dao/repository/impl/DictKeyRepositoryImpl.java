package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DictKeyQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DictKeyEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.DictKeyMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDictKeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色菜单 数据库操作封装实现层
 *
 * @author dqcer
 * @since 2023/12/01
 */
@Service
@Slf4j
public class DictKeyRepositoryImpl
        extends CrudRepository<DictKeyMapper, DictKeyEntity> implements IDictKeyRepository {

    @Override
    public void insert(DictKeyEntity entity) {
        baseMapper.insert(entity);
    }


    @Override
    public List<DictKeyEntity> getListAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public Page<DictKeyEntity> selectPage(DictKeyQueryDTO dto) {
        LambdaQueryWrapper<DictKeyEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getSearchWord();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(DictKeyEntity::getKeyName, keyword)
                    .or().like(DictKeyEntity::getKeyCode, keyword)
            );
        }
        query.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public DictKeyEntity insert(String keyCode, String keyName, String remark) {
        if (StrUtil.isBlank(keyName) || StrUtil.isBlank(keyCode)) {
            throw new IllegalArgumentException("keyName or keyCode is null");
        }
        DictKeyEntity entity = new DictKeyEntity();
        entity.setKeyCode(keyCode);
        entity.setKeyName(keyName);
        entity.setRemark(remark);
        this.insert(entity);
        return entity;
    }

    @Override
    public void update(Integer dictKeyId, String keyCode, String keyName, String remark) {
        DictKeyEntity entity = new DictKeyEntity();
        entity.setId(dictKeyId);
        entity.setKeyCode(keyCode);
        entity.setKeyName(keyName);
        entity.setRemark(remark);
        this.updateById(entity);
    }
}
