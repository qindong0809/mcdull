package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.InstanceListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.InstanceDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.InstanceMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IInstanceRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.entity.MiddleDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
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
public class InstanceRepositoryImpl extends ServiceImpl<InstanceMapper, InstanceDO>  implements IInstanceRepository {

    @Override
    public Page<InstanceDO> selectPage(InstanceListDTO dto) {
        LambdaQueryWrapper<InstanceDO> lambda = new QueryWrapper<InstanceDO>().lambda();
        lambda.eq(InstanceDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        lambda.eq(ObjUtil.isNotNull(dto.getGroupId()), InstanceDO::getGroupId, dto.getGroupId());
        lambda.orderByDesc(MiddleDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<InstanceDO> getListByName(String name) {
        LambdaQueryWrapper<InstanceDO> query = Wrappers.lambdaQuery();
        query.eq(InstanceDO::getName, name);
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        LambdaUpdateWrapper<InstanceDO> update = Wrappers.lambdaUpdate();
        update.set(BaseDO::getDelFlag, DelFlayEnum.DELETED.getCode());
        update.set(BaseDO::getDelBy, UserContextHolder.currentUserId());
        update.eq(IdDO::getId, id);
        baseMapper.update(null, update);
    }

    @Override
    public List<InstanceDO> getByGroupId(Long groupId) {
        LambdaQueryWrapper<InstanceDO> query = Wrappers.lambdaQuery();
        query.eq(InstanceDO::getGroupId, groupId);
        query.eq(BaseDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        List<InstanceDO> list = baseMapper.selectList(query);
        if (CollUtil.isNotEmpty(list)) {
            return list;
        }
        return Collections.emptyList();
    }
}