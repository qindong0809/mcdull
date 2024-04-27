package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitEntity;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.GitMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGitRepository;
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
public class GitRepositoryImpl extends ServiceImpl<GitMapper, GitEntity>  implements IGitRepository {

    @Override
    public Page<GitEntity> selectPage(GitListDTO dto) {
        LambdaQueryWrapper<GitEntity> lambda = new QueryWrapper<GitEntity>().lambda();
        lambda.orderByDesc(RelEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<GitEntity> getListByName(String name) {
        LambdaQueryWrapper<GitEntity> query = Wrappers.lambdaQuery();
        query.eq(GitEntity::getName, name);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<GitEntity> allList() {
        LambdaQueryWrapper<GitEntity> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }
}