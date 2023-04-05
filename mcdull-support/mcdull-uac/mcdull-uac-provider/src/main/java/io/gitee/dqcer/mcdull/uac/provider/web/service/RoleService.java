package io.gitee.dqcer.mcdull.uac.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
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
 * @since  2022/11/27
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
    public Result<PagedVO<RoleVO>> listByPage(RoleLiteDTO dto) {
        Page<RoleDO> entityPage = roleRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleDO entity : entityPage.getRecords()) {
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
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(RoleDO::getName, dto.getName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleDO> list = roleRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }

        RoleDO entity = RoleConvert.dto2Entity(dto);

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


        RoleDO dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        Integer status = dto.getStatus();
//        if (dbData.getStatus().equals(status)) {
//            log.warn("数据已存在 id: {} status: {}", id, status);
//            return Result.error(CodeEnum.DATA_EXIST);
//        }

        RoleDO entity = new RoleDO();
        entity.setId(id);
//        entity.setStatus(status);
        entity.setUpdatedBy(UserContextHolder.currentUserId());
        entity.setUpdatedTime(new Date());
        boolean success = roleRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
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


        RoleDO dbData = roleRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }
        Boolean delFlag = dto.getDelFlag();
        if (dbData.getDelFlag().equals(delFlag)) {
            log.warn("数据已存在 id: {} status: {}", id, delFlag);
            return Result.error(CodeEnum.DATA_EXIST);
        }

        RoleDO entity = new RoleDO();
        entity.setId(id);
        entity.setUpdatedBy(UserContextHolder.currentUserId());
        entity.setUpdatedTime(new Date());
        boolean success = roleRepository.updateById(entity);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }

        return Result.ok(id);
    }
}
