package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleUserQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserRepository {

    @Override
    public Page<UserEntity> selectPage(UserListDTO dto, List<Integer> deptIdList) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserEntity::getLoginName, keyword)
                    .or().like(UserEntity::getPhone, keyword)
            );
        }
        if (CollUtil.isNotEmpty(deptIdList)) {
            query.in(UserEntity::getDepartmentId, deptIdList);
        }
        Boolean disabledFlag = dto.getDisabledFlag();
        if (ObjUtil.isNotNull(disabledFlag)) {
            query.eq(BaseEntity::getInactive, disabledFlag);
        }
        query.orderByDesc(BaseEntity::getCreatedTime);
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Integer insert(UserEntity entity) {
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 单个根据账户名称
     *
     * @param loginName 账户
     * @return {@link UserEntity}
     */
    @Override
    public UserEntity get(String loginName) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.eq(UserEntity::getLoginName, loginName);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserEntity> list = list(query);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean update(Integer id, boolean inactive) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setInactive(inactive);
        return this.update(entity);
    }

    @Override
    public boolean update(Integer id, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setLoginPwd(password);
        return this.update(entity);
    }

    @Override
    public Page<UserEntity> selectPageByRoleId(List<Integer> userIdList, RoleUserQueryDTO dto) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserEntity::getLoginName, keyword)
                    .or().like(UserEntity::getPhone, keyword)
            );
        }

        if (CollUtil.isNotEmpty(userIdList)) {
            query.in(IdEntity::getId, userIdList);
        }
        query.orderByDesc(BaseEntity::getCreatedTime);
        Boolean notNeedPaged = dto.getNotNeedPaged();
        if (BooleanUtil.isTrue(notNeedPaged)) {
            List<UserEntity> userEntities = baseMapper.selectList(query);
            Page<UserEntity> objectPage = new Page<>();
            objectPage.setRecords(userEntities);
            return objectPage;
        }
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public Page<UserEntity> selectPageByRoleId(List<Integer> userIdList, PagedDTO dto) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserEntity::getLoginName, keyword)
                    .or().like(UserEntity::getPhone, keyword)
            );
        }

        if (CollUtil.isNotEmpty(userIdList)) {
            query.in(IdEntity::getId, userIdList);
        }
        query.orderByDesc(BaseEntity::getCreatedTime);
        Boolean notNeedPaged = dto.getNotNeedPaged();
        if (BooleanUtil.isTrue(notNeedPaged)) {
            List<UserEntity> userEntities = baseMapper.selectList(query);
            Page<UserEntity> objectPage = new Page<>();
            objectPage.setRecords(userEntities);
            return objectPage;
        }
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public List<UserEntity> listByDeptList(List<Integer> deptIdList) {
        if (CollUtil.isNotEmpty(deptIdList)) {
            LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
            query.in(UserEntity::getDepartmentId, deptIdList);
            return baseMapper.selectList(query);
        }
        return Collections.emptyList();
    }

    public boolean update(UserEntity entity) {
        return this.updateById(entity);
    }
}
