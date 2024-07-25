package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.ConfigMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* 系统配置 数据库操作封装实现层
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class ConfigRepositoryImpl
        extends ServiceImpl<ConfigMapper, ConfigEntity>  implements IConfigRepository {

    @Override
    public List<ConfigEntity> queryListByIds(List<Integer> idList) {
        LambdaQueryWrapper<ConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ConfigEntity::getId, idList);
        List<ConfigEntity> list =  baseMapper.selectList(wrapper);
        if (ObjUtil.isNotNull(list)) {
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public Page<ConfigEntity> selectPage(ConfigQueryDTO param) {
        LambdaQueryWrapper<ConfigEntity> lambda = new QueryWrapper<ConfigEntity>().lambda();
        String configKey = param.getConfigKey();
        if (StrUtil.isNotBlank(configKey)) {
            lambda.like(ConfigEntity::getConfigKey, configKey);
        }
        lambda.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        return baseMapper.selectPage(new Page<>(param.getPageNum(), param.getPageSize()), lambda);
    }

    @Override
    public ConfigEntity getById(Integer id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Integer insert(ConfigEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public boolean exist(ConfigEntity entity) {
        return !baseMapper.selectList(Wrappers.lambdaQuery(entity)).isEmpty();
    }

    @Override
    public ConfigEntity selectOne(String key) {
        LambdaQueryWrapper<ConfigEntity> query = Wrappers.lambdaQuery();
        query.eq(ConfigEntity::getConfigKey, key);
        return baseMapper.selectOne(query);
    }

    @Override
    public void deleteBatchByIds(List<Integer> ids) {
       baseMapper.deleteBatchIds(ids);
    }
}