package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.audit.RoleAudit;
import io.gitee.dqcer.mcdull.system.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Role Service Impl
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class RoleServiceImpl
        extends BasicServiceImpl<IRoleRepository> implements IRoleService {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleMenuService roleMenuService;

    @Resource
    private IAuditManager auditManager;

    @Override
    public RoleVO detail(Integer id) {
        RoleEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        return RoleConvert.entityToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(RoleAddDTO dto) {
        List<RoleEntity> roleEntityList = baseRepository.list();
        if (CollUtil.isNotEmpty(roleEntityList)) {
            this.validNameExist(null, dto.getRoleName(), roleEntityList,
                    roleEntity -> roleEntity.getRoleName().equals(dto.getRoleName()));
            this.validNameExist(null, dto.getRoleCode(), roleEntityList,
                    roleEntity -> roleEntity.getRoleCode().equals(dto.getRoleCode()));
        }
        RoleEntity entity = RoleConvert.insertToEntity(dto);
        baseRepository.insert(entity);
        auditManager.saveByAddEnum(dto.getRoleName(), entity.getId(), this.buildAuditLog(entity));
    }

    private Audit buildAuditLog(RoleEntity entity) {
        RoleAudit audit = new RoleAudit();
        audit.setRoleName(entity.getRoleName());
        audit.setRoleCode(entity.getRoleCode());
        audit.setRemark(entity.getRemark());
        return audit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        RoleEntity dbData = baseRepository.getById(id);
        if (ObjUtil.isNull(dbData)) {
            this.throwDataNotExistException(id);
        }
        baseRepository.removeById(id);
        auditManager.saveByDeleteEnum(dbData.getRoleName(), id, null);
    }

    @Override
    public Map<Integer, List<RoleEntity>> getRoleMap(List<Integer> userIdList) {
        Map<Integer, List<Integer>> userRoleMap = userRoleService.getRoleIdListMap(userIdList);
        if (CollUtil.isNotEmpty(userRoleMap)) {
            return baseRepository.roleListMap(userRoleMap);
        }
        return MapUtil.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, RoleUpdateDTO dto) {
        LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleEntity::getRoleName, dto.getRoleName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleEntity> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            RoleEntity role = list.get(0);
            if (!role.getId().equals(id)) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        RoleEntity role = baseRepository.getById(id);
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setRemark(dto.getRemark());
        baseRepository.updateById(role);
        auditManager.saveByUpdateEnum(dto.getRoleName(), id, this.buildAuditLog(role),
                this.buildAuditLog(baseRepository.getById(id)));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertPermission(Integer id, RolePermissionInsertDTO dto) {
        return roleMenuService.deleteAndInsert(id, dto.getMenuIdList());
    }

    @Override
    public List<RoleVO> all() {
        List<RoleVO> list = new ArrayList<>();
        List<RoleEntity> roleEntityList = baseRepository.list();
        if (CollUtil.isNotEmpty(roleEntityList)) {
            for (RoleEntity dept : roleEntityList) {
                RoleVO vo = RoleConvert.entityToVO(dept);
                list.add(vo);
            }
        }
        return list;
    }

    @Override
    public RoleVO get(Integer roleId) {
        RoleEntity entity = baseRepository.getById(roleId);
        if (ObjUtil.isNotNull(entity)) {
            return RoleConvert.entityToVO(entity);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateDTO dto) {
        Integer roleId = dto.getRoleId();
        RoleEntity entity = baseRepository.getById(roleId);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(roleId);
        }
        List<RoleEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(roleId, dto.getRoleName(), list,
                    i -> (!roleId.equals(i.getId())) && i.getRoleName().equals(dto.getRoleName()));
            this.validNameExist(roleId, dto.getRoleCode(), list,
                    i -> (!dto.getRoleId().equals(i.getId())) && i.getRoleCode().equals(dto.getRoleCode()));
        }
        entity.setRoleName(dto.getRoleName());
        entity.setRoleCode(dto.getRoleCode());
        entity.setRemark(dto.getRemark());
        baseRepository.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleMenu(RoleMenuUpdateDTO dto) {
        roleMenuService.deleteAndInsert(dto.getRoleId(), dto.getMenuIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemoveRoleEmployee(RoleEmployeeUpdateDTO dto) {
        userRoleService.batchRemoveUserListByRole(dto.getRoleId(), dto.getEmployeeIdList());
    }

    @Override
    public Map<Integer, String> mapName(List<Integer> roleIdList) {
        if (CollUtil.isNotEmpty(roleIdList)) {
            List<RoleEntity> list = baseRepository.listByIds(roleIdList);
            return list.stream().collect(Collectors.toMap(RoleEntity::getId, RoleEntity::getRoleName));
        }
        return Collections.emptyMap();
    }
}
