package com.dqcer.mcdull.uac.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dqcer.framework.base.auth.UserContextHolder;
import com.dqcer.framework.base.constants.SysConstants;
import com.dqcer.framework.base.exception.BusinessException;
import com.dqcer.framework.base.page.PageUtil;
import com.dqcer.framework.base.page.Paged;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.framework.base.wrapper.ResultCode;
import com.dqcer.mcdull.uac.api.convert.RoleConvert;
import com.dqcer.mcdull.uac.api.dto.RoleLiteDTO;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.RoleEntity;
import com.dqcer.mcdull.uac.api.vo.RoleVO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import com.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户服务
 *
 * @author dqcer
 * @version  2022/11/27
 */
@Service
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);


    @Resource
    private IRoleRepository roleRepository;

    @Resource
    private IRoleManager roleManager;
    
    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    public Result<Paged<RoleVO>> listByPage(RoleLiteDTO dto) {
        Page<RoleEntity> entityPage = roleRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleEntity entity : entityPage.getRecords()) {
            voList.add(roleManager.entity2VO(entity));
        }
        return Result.ok(PageUtil.toPage(voList, entityPage));
    }

    /**
     * 详情
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    public Result<RoleVO> detail(RoleLiteDTO dto) {
        return Result.ok(roleManager.entity2VO(roleRepository.getById(dto.getId())));
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> insert(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleEntity::getName, dto.getName());
        query.last(SysConstants.LAST_SQL_LIMIT_1);
        List<RoleEntity> list = roleRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(ResultCode.DATA_EXIST);
        }

        RoleEntity entity = RoleConvert.dto2Entity(dto);

        return Result.ok(roleRepository.insert(entity));
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> updateStatus(RoleLiteDTO dto) {
        Long id = dto.getId();


        RoleEntity dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(ResultCode.DATA_NOT_EXIST);
        }
        Integer status = dto.getStatus();
        if (dbData.getStatus().equals(status)) {
            log.warn("数据已存在 id: {} status: {}", id, status);
            return Result.error(ResultCode.DATA_EXIST);
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(id);
        entity.setStatus(status);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        entity.setUpdatedTime(new Date());
        boolean success = roleRepository.updateById(entity);
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


        RoleEntity dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(ResultCode.DATA_NOT_EXIST);
        }
        Integer delFlag = dto.getDelFlag();
        if (dbData.getDelFlag().equals(delFlag)) {
            log.warn("数据已存在 id: {} status: {}", id, delFlag);
            return Result.error(ResultCode.DATA_EXIST);
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(id);
        entity.setDelFlag(delFlag);
        entity.setUpdatedBy(UserContextHolder.getSession().getUserId());
        entity.setUpdatedTime(new Date());
        boolean success = roleRepository.updateById(entity);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(ResultCode.DB_ERROR);
        }

        return Result.ok(id);
    }
}
