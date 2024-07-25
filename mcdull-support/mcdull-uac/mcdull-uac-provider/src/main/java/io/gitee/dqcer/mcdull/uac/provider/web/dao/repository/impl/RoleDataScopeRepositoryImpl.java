package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.RoleDataScopeMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleDataScopeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role data scope repository impl
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Service
public class RoleDataScopeRepositoryImpl extends ServiceImpl<RoleDataScopeMapper, RoleDataScopeEntity>
        implements IRoleDataScopeRepository {

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
            this.executeBatch(insertList, insertList.size(), (sqlSession, entity) -> {
                sqlSession.getMapper(RoleDataScopeMapper.class).insert(entity);
            });
        }
        if (CollUtil.isNotEmpty(updateList)) {
            this.executeBatch(updateList, updateList.size(), (sqlSession, entity) -> {
                sqlSession.getMapper(RoleDataScopeMapper.class).updateById(entity);
            });
        }
        if (CollUtil.isNotEmpty(removeList)) {
            this.executeBatch(removeList, removeList.size(), (sqlSession, entity) -> {
                sqlSession.getMapper(RoleDataScopeMapper.class).deleteById(entity.getId());
            });
        }
    }
}