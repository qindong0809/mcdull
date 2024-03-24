package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GitListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GitDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.GitMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGitRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.RelDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* Instance 数据库操作封装实现层
*
* @author dqcer
* @since 2023-01-14
*/
@Service
public class GitRepositoryImpl extends ServiceImpl<GitMapper, GitDO>  implements IGitRepository {

    @Override
    public Page<GitDO> selectPage(GitListDTO dto) {
        LambdaQueryWrapper<GitDO> lambda = new QueryWrapper<GitDO>().lambda();
        lambda.orderByDesc(RelDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), lambda);
    }

    @Override
    public List<GitDO> getListByName(String name) {
        LambdaQueryWrapper<GitDO> query = Wrappers.lambdaQuery();
        query.eq(GitDO::getName, name);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<GitDO> allList() {
        LambdaQueryWrapper<GitDO> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }
}