package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class RoleServiceImpl extends BasicServiceImpl<IRoleRepository> implements IRoleService {

    @Resource
    private IRoleManager roleManager;

    @Resource
    private IUserRoleService userRoleService;
    

    @Override
    public PagedVO<RoleVO> listByPage(RoleLiteDTO dto) {
        Page<RoleDO> entityPage = baseRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleDO entity : entityPage.getRecords()) {
            voList.add(roleManager.entity2VO(entity));
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public Result<RoleVO> detail(RoleLiteDTO dto) {
        return Result.success(roleManager.entity2VO(baseRepository.getById(dto.getId())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> insert(RoleLiteDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(RoleDO::getName, dto.getName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleDO> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }

        RoleDO entity = RoleConvert.dto2Entity(dto);

        return Result.success(baseRepository.insert(entity));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateStatus(RoleLiteDTO dto) {
        Integer id = dto.getId();


        RoleDO dbData = baseRepository.getById(id);
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

        boolean success = baseRepository.updateById(entity);
        if (!success) {
            log.error("数据更新失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }

        return Result.success(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> delete(UserLiteDTO dto) {
        Integer id = dto.getId();


        RoleDO dbData = baseRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            return Result.error(CodeEnum.DATA_NOT_EXIST);
        }

        RoleDO entity = new RoleDO();
        entity.setId(id);

        boolean success = baseRepository.updateById(entity);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(CodeEnum.DB_ERROR);
        }

        return Result.success(id);
    }

    @Override
    public Map<Integer, List<RoleDO>> getRoleMap(List<Integer> userIdList) {
        Map<Integer, List<Integer>> userRoleMap = userRoleService.getRoleIdListMap(userIdList);
        if (CollUtil.isNotEmpty(userRoleMap)) {
            return baseRepository.roleListMap(userRoleMap);
        }
        return MapUtil.empty();
    }

}
