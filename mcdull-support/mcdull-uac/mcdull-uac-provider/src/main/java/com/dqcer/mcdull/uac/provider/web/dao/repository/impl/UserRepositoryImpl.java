package com.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.entity.BaseDO;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.util.ObjUtil;
import com.dqcer.framework.base.util.StrUtil;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.UserDO;
import com.dqcer.mcdull.uac.provider.web.dao.mapper.UserMapper;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, UserDO> implements IUserRepository {

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
//        entity.setDelFlag(DelFlayEnum.NORMAL.getCode());
//        entity.setStatus(StatusEnum.ENABLE.getCode());
//        entity.setCreatedBy(UserContextHolder.getSession().getUserId());
//        entity.setCreatedTime(new Date());
        int row = baseMapper.insert(entity);
        if (row == GlobalConstant.Database.ROW_0) {
            throw new BusinessException(ResultCode.DB_ERROR);
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
            return Collections.emptyList();
        }
        for (UserPowerVO vo : vos) {
            List<String> modules = baseMapper.queryModulesByRoleId(vo.getRoleId());
            if (ObjUtil.isNull(modules)) {
                vo.setModules(Collections.emptyList());
            }
            vo.setModules(modules);
        }
        return vos;
    }
}
