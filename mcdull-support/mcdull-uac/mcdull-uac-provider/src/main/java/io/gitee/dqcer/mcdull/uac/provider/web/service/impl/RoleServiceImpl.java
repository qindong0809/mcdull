package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.uac.IRoleManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Resource
    private IRoleMenuService roleMenuService;
    

    @Override
    public PagedVO<RoleVO> listByPage(RolePageDTO dto) {
        Page<RoleEntity> entityPage = baseRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleEntity entity : entityPage.getRecords()) {
            RoleVO vo = RoleConvert.entityToVO(entity);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public RoleVO detail(RoleLiteDTO dto) {
        return roleManager.entity2VO(baseRepository.getById(dto.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(RoleInsertDTO dto) {
        LambdaQueryWrapper<RoleEntity> query = Wrappers.lambdaQuery();
        query.eq(RoleEntity::getRoleName, dto.getRoleName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleEntity> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            throw new BusinessException(I18nConstants.NAME_DUPLICATED);
        }
        RoleEntity entity = RoleConvert.insertToEntity(dto);
        baseRepository.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long delete(UserListDTO dto) {
//        Long id = dto.getId();
//
//
//        RoleEntity dbData = baseRepository.getById(id);
//        if (null == dbData) {
//            log.warn("数据不存在 id:{}", id);
////            return Result.error(CodeEnum.DATA_NOT_EXIST);
//        }
//
//        RoleEntity entity = new RoleEntity();
//        entity.setId(id);
//
//        boolean success = baseRepository.updateById(entity);
//        if (!success) {
//            log.error("数据删除失败，entity:{}", entity);
//            throw new BusinessException(CodeEnum.DB_ERROR);
//        }
//
//        return id;
        return null;
    }

    @Override
    public Map<Long, List<RoleEntity>> getRoleMap(List<Long> userIdList) {
        Map<Long, List<Long>> userRoleMap = userRoleService.getRoleIdListMap(userIdList);
        if (CollUtil.isNotEmpty(userRoleMap)) {
            return baseRepository.roleListMap(userRoleMap);
        }
        return MapUtil.empty();
    }

    @Override
    public List<LabelValueVO<Long, String>> getSimple(Long userId) {
        List<LabelValueVO<Long, String>> list = new ArrayList<>();
        Map<Long, List<RoleEntity>> roleMap = this.getRoleMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleMap)) {
            for (Map.Entry<Long, List<RoleEntity>> entry : roleMap.entrySet()) {
                for (RoleEntity role : entry.getValue()) {
                    list.add(new LabelValueVO<>(role.getId(), role.getRoleName()));
                }
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Long id, RoleUpdateDTO dto) {
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
        return baseRepository.updateById(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean toggleStatus(Long id, ReasonDTO dto) {
        RoleEntity role = baseRepository.getById(id);
        if (ObjUtil.isNull(role)) {
            throw new BusinessException(I18nConstants.DATA_NEED_REFRESH);
        }
        return baseRepository.toggleStatus(id, !role.getInactive());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertPermission(Long id, RolePermissionInsertDTO dto) {
        return roleMenuService.deleteAndInsert(id, dto.getMenuIdList());
    }

    @Override
    public Map<Long, List<RoleEntity>> getRoleMapByMenuId(List<Long> menuIdList) {
        Map<Long, List<RoleEntity>> map = MapUtil.newHashMap();
        Map<Long, List<Long>> roleIdMap = roleMenuService.getRoleIdMap(menuIdList);
        if (MapUtil.isNotEmpty(roleIdMap)) {
            Set<Long> roleIdSet = roleIdMap.values().stream().flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            List<RoleEntity> listByIds = baseRepository.listByIds(new ArrayList<>(roleIdSet));
            if (CollUtil.isNotEmpty(listByIds)) {
                Map<Long, RoleEntity> roleMap = listByIds.stream()
                        .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
                for (Map.Entry<Long, List<Long>> entry : roleIdMap.entrySet()) {
                    Long menuId = entry.getKey();
                    List<Long> value = entry.getValue();
                    List<RoleEntity> roleList = new ArrayList<>();
                    for (Long roleId : value) {
                        RoleEntity role = roleMap.get(roleId);
                        if (ObjUtil.isNotNull(role)) {
                            roleList.add(role);
                        }
                    }
                    if (CollUtil.isNotEmpty(roleList)) {
                        map.put(menuId, roleList);
                    }
                }
            }
        }
        return map;
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
    public RoleVO get(Long roleId) {
        RoleEntity entity = baseRepository.getById(roleId);
        if (ObjUtil.isNotNull(entity)) {
            return RoleConvert.entityToVO(entity);
        }
        return null;
    }

}
