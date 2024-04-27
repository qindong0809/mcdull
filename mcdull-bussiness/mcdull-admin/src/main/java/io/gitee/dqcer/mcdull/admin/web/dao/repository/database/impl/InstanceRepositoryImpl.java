package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.InstanceMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IInstanceRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* Instance 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class InstanceRepositoryImpl extends ServiceImpl<InstanceMapper, InstanceEntity>  implements IInstanceRepository {

    @Override
    public Page<InstanceEntity> selectPage(InstanceListDTO dto) {
        LambdaQueryWrapper<InstanceEntity> lambda = new QueryWrapper<InstanceEntity>().lambda();
        lambda.eq(ObjUtil.isNotNull(dto.getGroupId()), InstanceEntity::getGroupId, dto.getGroupId());
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<InstanceEntity> getListByName(String name) {
        LambdaQueryWrapper<InstanceEntity> query = Wrappers.lambdaQuery();
        query.eq(InstanceEntity::getName, name);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<InstanceEntity> getByGroupId(Long groupId) {
        LambdaQueryWrapper<InstanceEntity> query = Wrappers.lambdaQuery();
        query.eq(InstanceEntity::getGroupId, groupId);
        List<InstanceEntity> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }
}