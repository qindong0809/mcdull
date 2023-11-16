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
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserRoleDO;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.MenuMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.RoleMenuMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.mapper.sys.UserRoleMapper;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.enums.DelFlayEnum;
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
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements IUserRepository {

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
     * @return {@link Page}<{@link UserDO}>
     */

    @Override
    public Page<UserDO> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        String account = dto.getAccount();
        if (StrUtil.isNotBlank(account)) {
            query.like(UserDO::getAccount, account);
        }
        String status = dto.getStatus();
        if (StrUtil.isNotBlank(status)) {
            query.eq(UserDO::getStatus, status);
        }
        Long deptId = dto.getDeptId();
        if (ObjUtil.isNotNull(deptId)) {
            query.eq(UserDO::getDeptId, deptId);
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        query.eq(UserDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return baseMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), query);
    }

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    @Override
    public Long insert(UserDO entity) {
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
     * @return {@link UserDO}
     */
    @Override
    public UserDO oneByAccount(String account) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getAccount, account);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<UserDO> list = list(query);
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
        LambdaQueryWrapper<UserRoleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.select(UserRoleDO::getRoleId);
        wrapper.eq(UserRoleDO::getUserId, userId);
        List<UserRoleDO> userRoleDOList = userRoleMapper.selectList(wrapper);
        if (CollUtil.isEmpty(userRoleDOList)) {
            log.warn("userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        List<UserPowerVO> vos = new ArrayList<>();

        for (UserRoleDO userRoleDO : userRoleDOList) {
            Long roleId = userRoleDO.getRoleId();
            RoleDO roleDO = roleMapper.selectById(roleId);
            if (DelFlayEnum.DELETED.getCode().equals(roleDO.getDelFlag())) {
                continue;
            }

            UserPowerVO vo = this.builderPowerVO(roleDO);
            vos.add(vo);
            LambdaQueryWrapper<RoleMenuDO> wrapperRoelMenu = Wrappers.lambdaQuery();
            wrapperRoelMenu.select(RoleMenuDO::getMenuId);
            wrapperRoelMenu.eq(RoleMenuDO::getRoleId, roleId);
            List<RoleMenuDO> roleMenuDOList = roleMenuMapper.selectList(wrapperRoelMenu);
            if (CollUtil.isNotEmpty(roleMenuDOList)) {
                List<Long> menuIds = roleMenuDOList.stream().map(RoleMenuDO::getMenuId).collect(Collectors.toList());
                LambdaQueryWrapper<MenuDO> wrapperMenu = Wrappers.lambdaQuery();
                wrapperMenu.in(IdDO::getId, menuIds);
                wrapperMenu.eq(MenuDO::getStatus, StatusEnum.ENABLE.getCode());
                List<MenuDO> list = menuMapper.selectList(wrapperMenu);
                if (CollUtil.isEmpty(list)) {
                    log.warn("userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                    continue;
                }
                List<String> perms = list.stream().map(MenuDO::getPerms).collect(Collectors.toList());
                vo.setModules(perms);
            }
        }
        return vos;
    }

    private UserPowerVO builderPowerVO(RoleDO roleDO) {
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
        LambdaUpdateWrapper<UserDO> update = Wrappers.lambdaUpdate();
        update.set(UserDO::getLastLoginTime, new Date());
        update.eq(IdDO::getId, userId);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    /**
     * 查询用户帐户
     *
     * @param account 账户
     * @return {@link UserDO}
     */
    @Override
    public UserDO queryUserByAccount(String account) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getAccount, account);
        List<UserDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void delete(Long id) {
        LambdaUpdateWrapper<UserDO> update = Wrappers.lambdaUpdate();
        update.set(UserDO::getDelFlag, DelFlayEnum.DELETED.getCode());
        update.eq(IdDO::getId, id);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }

    @Override
    public void updateStatusById(Long id, String status) {
        LambdaUpdateWrapper<UserDO> update = Wrappers.lambdaUpdate();
        update.set(UserDO::getStatus, status);
        update.eq(IdDO::getId, id);
        int rowSize = baseMapper.update(null, update);
        if (rowSize == GlobalConstant.Database.ROW_0) {
            throw new DatabaseRowException(CodeEnum.DB_ERROR);
        }
    }
}
