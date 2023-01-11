package com.dqcer.mcdull.admin.web.dao.repository.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.entity.BaseDO;
import com.dqcer.framework.base.entity.IdDO;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.exception.DatabaseRowException;
import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.framework.base.util.StrUtil;
import com.dqcer.framework.base.wrapper.CodeEnum;
import com.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.admin.web.dao.mapper.sys.UserMapper;
import com.dqcer.mcdull.admin.web.dao.repository.sys.IUserRepository;
import com.dqcer.mcdull.framework.web.config.ThreadPoolConfig;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 用户 数据库操作实现层
 *
 * @author dqcer
 * @date 2022/12/25
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserDO}>
     */

    @Override
    public Page<UserDO> selectPage(UserLiteDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        String keyword = dto.getKeyword();
        if (StrUtil.isNotBlank(keyword)) {
            query.and(i-> i.like(UserDO::getAccount, keyword).or().like(UserDO::getPhone, keyword).or().like(UserDO::getEmail, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
        query.eq(UserDO::getDelFlag, DelFlayEnum.NORMAL.getCode());
        return baseMapper.selectPage(new Page<>(dto.getCurrentPage(), dto.getPageSize()), query);
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
        List<UserPowerVO> vos = baseMapper.queryRoles(userId);
        if (ObjUtil.isNull(vos)) {
            log.warn("userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        for (UserPowerVO vo : vos) {
            List<String> modules = baseMapper.queryModulesByRoleId(vo.getRoleId());
            if (ObjUtil.isNull(modules)) {
                log.warn("userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                vo.setModules(Collections.emptyList());
            }
            vo.setModules(modules);
        }
        return vos;
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
            throw new DatabaseRowException();
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
        if (ObjUtil.isNull(list)) {
            return null;
        }
        return list.get(0);
    }
}
