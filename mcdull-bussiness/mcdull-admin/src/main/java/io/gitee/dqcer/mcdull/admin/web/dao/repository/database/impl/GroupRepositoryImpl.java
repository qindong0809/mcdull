package io.gitee.dqcer.mcdull.admin.web.dao.repository.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.database.GroupListDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.database.GroupDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.database.GroupMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.database.IGroupRepository;
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
public class GroupRepositoryImpl extends ServiceImpl<GroupMapper, GroupDO>  implements IGroupRepository {

    @Override
    public Page<GroupDO> selectPage(GroupListDTO dto) {
        LambdaQueryWrapper<GroupDO> lambda = new QueryWrapper<GroupDO>().lambda();
        lambda.orderByDesc(RelDO::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), lambda);
    }

    @Override
    public List<GroupDO> getListByName(String name) {
        LambdaQueryWrapper<GroupDO> query = Wrappers.lambdaQuery();
        query.eq(GroupDO::getName, name);
        return baseMapper.selectList(query);
    }

    @Override
    public void removeUpdate(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public List<GroupDO> allList() {
        LambdaQueryWrapper<GroupDO> query = Wrappers.lambdaQuery();
        return baseMapper.selectList(query);
    }
}