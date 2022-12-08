package com.dqcer.mcdull.uac.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.util.Md5Util;
import com.dqcer.framework.base.util.PageUtil;
import com.dqcer.framework.base.util.Sha1Util;
import com.dqcer.framework.base.vo.BaseVO;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.uac.api.convert.UserConvert;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.RoleDO;
import com.dqcer.mcdull.uac.api.entity.UserDO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IUserRoleRepository;
import com.dqcer.mcdull.uac.provider.web.manager.uac.IUserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户服务
 *
 * @author dqcer
 * @version  2022/11/27
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IUserManager userManager;

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IRoleRepository roleRepository;
    
    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    public Result<PagedVO<UserVO>> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = userRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        for (UserDO entity : entityPage.getRecords()) {
            UserVO vo = userManager.entity2VO(entity);
            List<Long> list = userRoleRepository.listRoleByUserId(vo.getId());
            List<BaseVO> baseRoles = new ArrayList<>();
            for (RoleDO roleDO : roleRepository.listByIds(list)) {
                BaseVO role = new BaseVO();
                role.setId(roleDO.getId());
                role.setName(roleDO.getName());
                baseRoles.add(role);
            }
            vo.setRoles(baseRoles);
            voList.add(vo);
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 详情
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    public Result<UserVO> detail(UserLiteDTO dto) {
        return Result.ok(userManager.entity2VO(userRepository.getById(dto.getId())));
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> insert(UserLiteDTO dto) {
        LambdaQueryWrapper<UserDO> query = Wrappers.lambdaQuery();
        query.eq(UserDO::getAccount, dto.getAccount());
        query.last(GlobalConstant.SQL_LIMIT_1);
        List<UserDO> list = userRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(ResultCode.DATA_EXIST);
        }

        UserDO entity = UserConvert.dto2Entity(dto);

        String salt = UUID.randomUUID().toString();
        String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
        entity.setSalt(salt);
        entity.setPassword(password);
        return Result.ok(userRepository.insert(entity));
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> updateStatus(UserLiteDTO dto) {
        Long id = dto.getId();


        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(ResultCode.DATA_NOT_EXIST);
        }
        Integer status = dto.getStatus();
        if (dbData.getStatus().equals(status)) {
            log.warn("数据已存在 id: {} status: {}", id, status);
            return Result.error(ResultCode.DATA_EXIST);
        }

        UserDO entity = new UserDO();
        entity.setId(id);
        entity.setStatus(status);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        entity.setUpdatedTime(new Date());
        boolean success = userRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new BusinessException(ResultCode.DB_ERROR);
        }

        return Result.ok(id);
    }

    /**
     * 删除
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> delete(UserLiteDTO dto) {
        Long id = dto.getId();


        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(ResultCode.DATA_NOT_EXIST);
        }
        Integer delFlag = dto.getDelFlag();
        if (dbData.getDelFlag().equals(delFlag)) {
            log.warn("数据已存在 id: {} status: {}", id, delFlag);
            return Result.error(ResultCode.DATA_EXIST);
        }

        UserDO entity = new UserDO();
        entity.setId(id);
        entity.setDelFlag(delFlag);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        entity.setUpdatedTime(new Date());
        boolean success = userRepository.updateById(entity);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(ResultCode.DB_ERROR);
        }

        return Result.ok(id);
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> resetPassword(UserLiteDTO dto) {
        Long id = dto.getId();
        UserDO entity = userRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(ResultCode.DATA_NOT_EXIST);
        }
        String password = Sha1Util.getSha1(Md5Util.getMd5(entity.getAccount() + entity.getSalt()));
        UserDO user = new UserDO();
        user.setId(id);
        user.setPassword(password);
        boolean success = userRepository.updateById(entity);
        if (!success) {
            log.error("重置密码失败，entity:{}", entity);
            throw new BusinessException(ResultCode.DB_ERROR);
        }
        return Result.ok(id);
    }
}
