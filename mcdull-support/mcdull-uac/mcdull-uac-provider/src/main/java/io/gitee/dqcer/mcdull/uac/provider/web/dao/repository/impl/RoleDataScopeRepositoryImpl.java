package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.RoleDataScopeMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleDataScopeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Service
public class RoleDataScopeRepositoryImpl extends ServiceImpl<RoleDataScopeMapper, RoleDataScopeEntity>
        implements IRoleDataScopeRepository {

    private static final Logger log = LoggerFactory.getLogger(RoleDataScopeRepositoryImpl.class);

    @Override
    public List<RoleDataScopeEntity> getListByRole(Integer roleId) {
        LambdaQueryWrapper<RoleDataScopeEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleDataScopeEntity::getRoleId, roleId);
        return baseMapper.selectList(query);
    }

    @Override
    public void update(List<RoleDataScopeEntity> insertList,
                       List<RoleDataScopeEntity> updateList,
                       List<RoleDataScopeEntity> removeList) {
        if (CollUtil.isNotEmpty(insertList)) {
            this.saveBatch(insertList, insertList.size());
        }
        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList, updateList.size());
        }
        if (CollUtil.isNotEmpty(removeList)) {
            this.removeBatchByIds(removeList, removeList.size());
        }
    }
}