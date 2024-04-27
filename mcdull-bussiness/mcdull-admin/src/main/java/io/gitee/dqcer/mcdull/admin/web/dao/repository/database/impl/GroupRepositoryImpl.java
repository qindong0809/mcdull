package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.GroupMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Instance 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class GroupRepositoryImpl extends ServiceImpl<GroupMapper, GroupEntity>  implements IGroupRepository {

    @Override
    public Page<GroupEntity> selectPage(GroupListDTO dto) {
        LambdaQueryWrapper<GroupEntity> lambda = new QueryWrapper<GroupEntity>().lambda();
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<GroupEntity> getListByName(String name) {
        LambdaQueryWrapper<GroupEntity> query = Wrappers.lambdaQuery();
        query.eq(GroupEntity::getName, name);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<GroupEntity> allList() {
        LambdaQueryWrapper<GroupEntity> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }
}