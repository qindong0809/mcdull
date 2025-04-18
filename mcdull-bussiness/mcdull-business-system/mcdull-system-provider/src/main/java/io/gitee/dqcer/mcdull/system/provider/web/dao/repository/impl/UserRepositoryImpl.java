package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.system.provider.model.dto.RoleUserQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.UserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User RepositoryImpl
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRepositoryImpl
        extends CrudRepository<UserMapper, UserEntity> implements IUserRepository {

    @Override
    public Page<UserEntity> selectPage(UserListDTO dto, List<Integer> deptIdList,
                                       List<Integer> notContainsUserIdList) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserEntity::getLoginName, keyword)
                    .or().like(UserEntity::getPhone, keyword)
                    .or().like(UserEntity::getActualName, keyword)
            );
        }
        if (CollUtil.isNotEmpty(deptIdList)) {
            query.in(UserEntity::getDepartmentId, deptIdList);
        }
        if (CollUtil.isNotEmpty(notContainsUserIdList)) {
            query.notIn(IdEntity::getId, notContainsUserIdList);
        }
        Boolean disabledFlag = dto.getDisabledFlag();
        if (ObjUtil.isNotNull(disabledFlag)) {
            query.eq(BaseEntity::getInactive, disabledFlag);
        }
        String sortField = dto.getSortField();
        if (StrUtil.isNotBlank(sortField)) {
            query.orderBy(sortField.equals(LambdaUtil.getFieldName(UserEntity::getActualName)), dto.isAsc(), UserEntity::getActualName);
            query.orderBy(sortField.equals(LambdaUtil.getFieldName(UserEntity::getLoginName)), dto.isAsc(), UserEntity::getLoginName);
            query.orderBy(sortField.equals(LambdaUtil.getFieldName(UserEntity::getInactive)), dto.isAsc(), UserEntity::getInactive);
            query.orderBy(sortField.equals(LambdaUtil.getFieldName(UserEntity::getCreatedTime)), dto.isAsc(), UserEntity::getCreatedTime);
            query.orderBy(sortField.equals(LambdaUtil.getFieldName(UserEntity::getUpdatedTime)), dto.isAsc(), UserEntity::getUpdatedTime);
        } else {
            query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
        }
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    @Override
    public Integer insert(UserEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

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

        UserEntity userEntity = this.getById(id);
        String usedPassword = userEntity.getUsedPassword();
        List<String> passwordList = new ArrayList<>();
        if (StrUtil.isNotBlank(usedPassword)) {
            boolean isArray = JSONUtil.isTypeJSONArray(usedPassword);
            if (isArray) {
                JSONArray objects = JSONUtil.parseArray(usedPassword);
                List<String> list = objects.toList(String.class);
                list.add(password);
                passwordList.addAll(list);
            }
        } else {
            passwordList.add(password);
        }
        entity.setUsedPassword(JSONUtil.toJsonStr(passwordList));
        entity.setLastPasswordModifiedDate(UserContextHolder.getSession().getNow());
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
        query.orderByDesc(ListUtil.of(RelEntity::getCreatedTime, RelEntity::getUpdatedTime));
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

    @Override
    public List<UserEntity> like(String userName) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.like(UserEntity::getActualName, userName);
        return baseMapper.selectList(query);
    }

    public boolean update(UserEntity entity) {
        return this.updateById(entity);
    }
}
