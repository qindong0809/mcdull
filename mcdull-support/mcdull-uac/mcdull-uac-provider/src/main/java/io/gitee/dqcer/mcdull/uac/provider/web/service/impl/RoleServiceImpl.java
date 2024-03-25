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
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.RoleConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
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
        Page<RoleDO> entityPage = baseRepository.selectPage(dto);
        List<RoleVO> voList = new ArrayList<>();
        for (RoleDO entity : entityPage.getRecords()) {
            RoleVO vo = RoleConvert.entityToVO(entity);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public Result<RoleVO> detail(RoleLiteDTO dto) {
        return Result.success(roleManager.entity2VO(baseRepository.getById(dto.getId())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(RoleInsertDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(RoleDO::getName, dto.getName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleDO> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            throw new BusinessException(I18nConstants.NAME_DUPLICATED);
        }
        RoleDO entity = RoleConvert.insertToEntity(dto);
        entity.setType(1);
        baseRepository.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> toggleStatus(RoleLiteDTO dto) {
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

    @Override
    public List<LabelValueVO<Integer, String>> getSimple(Integer userId) {
        List<LabelValueVO<Integer, String>> list = new ArrayList<>();
        Map<Integer, List<RoleDO>> roleMap = this.getRoleMap(ListUtil.of(userId));
        if (MapUtil.isNotEmpty(roleMap)) {
            for (Map.Entry<Integer, List<RoleDO>> entry : roleMap.entrySet()) {
                for (RoleDO role : entry.getValue()) {
                    list.add(new LabelValueVO<>(role.getId(), role.getName()));
                }
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, RoleUpdateDTO dto) {
        LambdaQueryWrapper<RoleDO> query = Wrappers.lambdaQuery();
        query.eq(RoleDO::getName, dto.getName());
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        List<RoleDO> list = baseRepository.list(query);
        if (!list.isEmpty()) {
            RoleDO role = list.get(0);
            if (!role.getId().equals(id)) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        RoleDO role = baseRepository.getById(id);
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return baseRepository.updateById(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean toggleStatus(Integer id, ReasonDTO dto) {
        RoleDO role = baseRepository.getById(id);
        if (ObjUtil.isNull(role)) {
            throw new BusinessException(I18nConstants.DATA_NEED_REFRESH);
        }
        return baseRepository.toggleStatus(id, !role.getInactive());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertPermission(Integer id, RolePermissionInsertDTO dto) {
        return roleMenuService.deleteAndInsert(id, dto.getMenuIdList());
    }

    @Override
    public Map<Integer, List<RoleDO>> getRoleMapByMenuId(List<Integer> menuIdList) {
        Map<Integer, List<RoleDO>> map = MapUtil.newHashMap();
        Map<Integer, List<Integer>> roleIdMap = roleMenuService.getRoleIdMap(menuIdList);
        if (MapUtil.isNotEmpty(roleIdMap)) {
            Set<Integer> roleIdSet = roleIdMap.values().stream().flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            List<RoleDO> listByIds = baseRepository.listByIds(new ArrayList<>(roleIdSet));
            if (CollUtil.isNotEmpty(listByIds)) {
                Map<Integer, RoleDO> roleMap = listByIds.stream()
                        .collect(Collectors.toMap(IdDO::getId, Function.identity()));
                for (Map.Entry<Integer, List<Integer>> entry : roleIdMap.entrySet()) {
                    Integer menuId = entry.getKey();
                    List<Integer> value = entry.getValue();
                    List<RoleDO> roleList = new ArrayList<>();
                    for (Integer roleId : value) {
                        RoleDO role = roleMap.get(roleId);
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

}
