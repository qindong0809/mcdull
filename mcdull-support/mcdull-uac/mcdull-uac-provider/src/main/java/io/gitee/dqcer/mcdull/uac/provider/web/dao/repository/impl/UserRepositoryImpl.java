package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.DatabaseRowException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户 数据库操作实现层
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements IUserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Resource
    private IMenuRepository menuRepository;

    @Resource
    private IRoleRepository roleRepository;

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
            query.and(i-> i.like(UserDO::getUsername, keyword)
                    .or().like(UserDO::getPhone, keyword)
                    .or().like(UserDO::getEmail, keyword));
        }
        query.orderByDesc(BaseDO::getCreatedTime);
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
        query.eq(UserDO::getUsername, account);
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
        Map<Long, List<RoleDO>> roleListMap = roleRepository.roleListMap(ListUtil.of(userId));
        List<RoleDO> roleDOList = roleListMap.get(userId);
        if (CollUtil.isEmpty(roleDOList)) {
            log.warn("userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        List<UserPowerVO> vos = roleDOList.stream().map(i-> {
            UserPowerVO vo = new UserPowerVO();
            vo.setRoleId(i.getId());
            vo.setCode(i.getCode());
            return vo;
        }).collect(Collectors.toList());

        Set<Long> roleSet = vos.stream().map(UserPowerVO::getRoleId).collect(Collectors.toSet());
        Map<Long, List<String>> keyRoleIdValueMenuCode = menuRepository.menuCodeListMap(roleSet);
        for (UserPowerVO vo : vos) {
            String code = vo.getCode();
            if (ObjectUtil.equals(GlobalConstant.SUPER_ADMIN_ROLE, code)) {
                List<String> allCodeList = menuRepository.allCodeList();
                vo.setModules(allCodeList);
                continue;
            }
            List<String> menuCodeList = keyRoleIdValueMenuCode.get(vo.getRoleId());
            if (CollUtil.isEmpty(menuCodeList)) {
                log.warn("userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                menuCodeList = Collections.emptyList();
            }
            vo.setModules(menuCodeList);
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
        wrapper.eq(UserDO::getUsername, account);
        List<UserDO> list = baseMapper.selectList(wrapper);
        if (ObjUtil.isNull(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public UserDO get(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUsername, username);
        List<UserDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
