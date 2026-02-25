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
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.web.util.LogicCheckUtil;
import io.gitee.dqcer.mcdull.system.provider.model.audit.RoleAudit;
import io.gitee.dqcer.mcdull.system.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.RoleMapper;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Role Service Impl
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class RoleServiceImpl
        extends BasicCurdServiceImpl<RoleMapper, RoleEntity> implements IRoleService {

    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRoleMenuService roleMenuService;
    @Resource
    private IAuditManager auditManager;

    @Override
    public RoleVO detail(Integer id) {
        RoleEntity entity = super.mustGet(id);
        return RoleConvert.entityToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(RoleAddDTO dto) {
        List<RoleEntity> roleEntityList = super.list();
        if (CollUtil.isNotEmpty(roleEntityList)) {
            LogicCheckUtil.validNameExist(null, dto.getRoleName(), roleEntityList,
                    roleEntity -> roleEntity.getRoleName().equals(dto.getRoleName()));
            LogicCheckUtil.validNameExist(null, dto.getRoleCode(), roleEntityList,
                    roleEntity -> roleEntity.getRoleCode().equals(dto.getRoleCode()));
        }
        RoleEntity entity = RoleConvert.insertToEntity(dto);
        baseMapper.insert(entity);
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
        RoleEntity dbData = super.mustGet(id);
        super.removeById(id);
        auditManager.saveByDeleteEnum(dbData.getRoleName(), id, null);
    }

    @Override
    public Map<Integer, List<RoleEntity>> getRoleMap(List<Integer> userIdList) {
        Map<Integer, List<Integer>> userRoleMap = userRoleService.getRoleIdListMap(userIdList);
        if (CollUtil.isNotEmpty(userRoleMap)) {
            return this.roleListMap(userRoleMap);
        }
        return MapUtil.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, RoleUpdateDTO dto) {
        LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleEntity::getRoleName, dto.getRoleName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleEntity> list = this.list(query);
        if (!list.isEmpty()) {
            RoleEntity role = list.get(0);
            if (!role.getId().equals(id)) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        RoleEntity role = super.getById(id);
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setRemark(dto.getRemark());
        this.updateById(role);
        auditManager.saveByUpdateEnum(dto.getRoleName(), id, this.buildAuditLog(role),
                this.buildAuditLog(this.getById(id)));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id, ReasonDTO dto) {
        return this.delete(id, dto.getReason());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertPermission(Integer id, RolePermissionInsertDTO dto) {
        return roleMenuService.deleteAndInsert(id, dto.getMenuIdList());
    }

    @Override
    public List<RoleVO> all() {
        List<RoleVO> list = new ArrayList<>();
        List<RoleEntity> roleEntityList = super.list();
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
        RoleEntity entity = super.mustGet(roleId);
        return RoleConvert.entityToVO(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateDTO dto) {
        Integer roleId = dto.getRoleId();
        RoleEntity entity = super.mustGet(roleId);
        List<RoleEntity> list = super.list();
        if (CollUtil.isNotEmpty(list)) {
            LogicCheckUtil.validNameExist(roleId, dto.getRoleName(), list,
                    i -> (!roleId.equals(i.getId())) && i.getRoleName().equals(dto.getRoleName()));
            LogicCheckUtil.validNameExist(roleId, dto.getRoleCode(), list,
                    i -> (!dto.getRoleId().equals(i.getId())) && i.getRoleCode().equals(dto.getRoleCode()));
        }
        entity.setRoleName(dto.getRoleName());
        entity.setRoleCode(dto.getRoleCode());
        entity.setRemark(dto.getRemark());
        super.updateById(entity);
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
            List<RoleEntity> list = this.listByIds(roleIdList);
            return list.stream().collect(Collectors.toMap(RoleEntity::getId, RoleEntity::getRoleName));
        }
        return Collections.emptyMap();
    }


    public Integer insert(RoleEntity entity) {
        baseMapper.insert(entity);
        return entity.getId();
    }

    public Map<Integer, List<RoleEntity>> roleListMap(Map<Integer, List<Integer>> userRoleMap) {
        Map<Integer, List<RoleEntity>> resultMap = new HashMap<>(userRoleMap.size());
        if (MapUtil.isNotEmpty(userRoleMap)) {
            Set<Integer> idList = userRoleMap.values().stream()
                .flatMap(Collection::stream).collect(Collectors.toSet());

            LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
            query.eq(RoleEntity::getInactive, InactiveEnum.FALSE.getCode());
            query.in(RoleEntity::getId, idList);
            List<RoleEntity> list = baseMapper.selectList(query);
            if (CollUtil.isNotEmpty(list)) {
                Map<Integer, RoleEntity> map = list.stream()
                    .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
                for (Map.Entry<Integer, List<Integer>> entry : userRoleMap.entrySet()) {
                    List<Integer> roleIdList = entry.getValue();
                    List<RoleEntity> roleList = roleIdList.stream().map(map::get)
                        .filter(ObjUtil::isNotEmpty).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(roleList)) {
                        resultMap.put(entry.getKey(), roleList);
                    }
                }
            }
        }
        return resultMap;
    }

    public boolean delete(Integer id, String reason) {
        return this.removeById(id);
    }

    public boolean toggleStatus(Integer id, boolean inactive) {
        RoleEntity role = new RoleEntity();
        role.setId(id);
        role.setInactive(inactive);
        return baseMapper.updateById(role) > 0;
    }
}
