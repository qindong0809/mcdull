package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.*;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.*;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.web.config.ThreadPoolConfig;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 数据库操作实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);


    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuMapper menuMapper;

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserEntity}>
     */

    @Override
    public Page<UserEntity> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        String account = dto.getAccount();
        if (StrUtil.isNotBlank(account)) {
            query.like(UserEntity::getAccount, account);
        }
        String status = dto.getStatus();
        if (StrUtil.isNotBlank(status)) {
            query.eq(UserEntity::getStatus, status);
        }
        Long deptId = dto.getDeptId();
        if (ObjUtil.isNotNull(deptId)) {
            query.eq(UserEntity::getDeptId, deptId);
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
    public Long insert(UserEntity entity) {
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(CodeEnum.DB_ERROR);
        }
        return entity.getId();
    }

    /**
     * 单个根据账户名称
     *
     * @param account 账户
     * @return {@link UserEntity}
     */
    @Override
    public UserEntity oneByAccount(String account) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.eq(UserEntity::getAccount, account);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserEntity> list = list(query);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserPowerVO}>
     */
    @Override
    public List<UserPowerVO> queryResourceModules(Long userId) {
        LambdaQueryWrapper<UserRoleEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.select(UserRoleEntity::getRoleId);
        wrapper.eq(UserRoleEntity::getUserId, userId);
        List<UserRoleEntity> userRoleDOList = userRoleMapper.selectList(wrapper);
        if (CollUtil.isEmpty(userRoleDOList)) {
            log.warn("userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        List<UserPowerVO> vos = new ArrayList<>();

        for (UserRoleEntity userRoleDO : userRoleDOList) {
            Long roleId = userRoleDO.getRoleId();
            RoleEntity roleDO = roleMapper.selectById(roleId);

            UserPowerVO vo = this.builderPowerVO(roleDO);
            vos.add(vo);
            LambdaQueryWrapper<RoleMenuEntity> wrapperRoelMenu = Wrappers.lambdaQuery();
            wrapperRoelMenu.select(RoleMenuEntity::getMenuId);
            wrapperRoelMenu.eq(RoleMenuEntity::getRoleId, roleId);
            List<RoleMenuEntity> roleMenuDOList = roleMenuMapper.selectList(wrapperRoelMenu);
            if (CollUtil.isNotEmpty(roleMenuDOList)) {
                List<Long> menuIds = roleMenuDOList.stream().map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
                LambdaQueryWrapper<MenuEntity> wrapperMenu = Wrappers.lambdaQuery();
                wrapperMenu.in(IdEntity::getId, menuIds);
                wrapperMenu.eq(MenuEntity::getStatus, StatusEnum.ENABLE.getCode());
                List<MenuEntity> list = menuMapper.selectList(wrapperMenu);
                if (CollUtil.isEmpty(list)) {
                    log.warn("userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                    continue;
                }
                List<String> perms = list.stream().map(MenuEntity::getPerms).collect(Collectors.toList());
                vo.setModules(perms);
            }
        }
        return vos;
    }

    private UserPowerVO builderPowerVO(RoleEntity roleDO) {
        UserPowerVO vo = new UserPowerVO();
        vo.setRoleId(roleDO.getId());
        vo.setCode(roleDO.getName());
//        vo.setRoleType(roleDO.getType());
        return vo;
    }

    /**
     * 更新登录时间通过id
     *
     * @param userId 用户id
     */
    @Override
    public void updateLoginTimeById(Long userId) {
        LambdaUpdateWrapper<UserEntity> update = Wrappers.lambdaUpdate();
        update.set(UserEntity::getLastLoginTime, new Date());
        update.eq(IdEntity::getId, userId);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 查询用户帐户
     *
     * @param account 账户
     * @return {@link UserEntity}
     */
    @Override
    public UserEntity queryUserByAccount(String account) {
        LambdaQueryWrapper<UserEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserEntity::getAccount, account);
        List<UserEntity> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void delete(Long id) {
        LambdaUpdateWrapper<UserEntity> update = Wrappers.lambdaUpdate();
        update.eq(IdEntity::getId, id);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    @Override
    public void updateStatusById(Long id, String status) {
        LambdaUpdateWrapper<UserEntity> update = Wrappers.lambdaUpdate();
        update.set(UserEntity::getStatus, status);
        update.eq(IdEntity::getId, id);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}
