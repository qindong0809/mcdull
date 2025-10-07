package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.RoleSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.*;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminRoleMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminMenuService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminRoleService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRoleEntity> implements IAdminRoleService {

    @Resource
    private IAdminMenuService adminMenuService;
    @Resource
    private IAdminUserService adminUserService;

    @Override
    public List<AdminRoleEntity> getList() {
        return super.list();
    }

    @Override
    public List<AdminRoleVO> getRoleList() {
        List<AdminRoleEntity> list = this.getList();
        List<AdminRoleVO> voList = new ArrayList<>();
        for (AdminRoleEntity entity : list) {
            AdminRoleVO vo = new AdminRoleVO();
            vo.setId(entity.getId());
            vo.setName(entity.getName());
            vo.setCode(entity.getCode());
            vo.setDescription(entity.getDescription());
            vo.setIsSystem(entity.getIsSystem());
            vo.setDataScope(entity.getDataScope());
            vo.setSort(entity.getSort());
            String menuJoin = entity.getMenuJoin();
            if (StrUtil.isNotBlank(menuJoin)) {
                List<String> split = StrUtil.split(menuJoin, StrUtil.COMMA);
                vo.setMenuIds(split.stream().map(Integer::parseInt).collect(Collectors.toList()));
            } else {
                vo.setMenuIds(new ArrayList<>());
            }
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public List<RolePermissionVO> getRoleTreeList() {
        List<MenuInfoBO> menuInfoBOS = adminMenuService.menuList();
        JSONArray objects = JSONUtil.parseArray(menuInfoBOS);
        List<RolePermissionVO> list = JSONUtil.toList(objects, RolePermissionVO.class);
        List<PermissionBO> permissionList = adminMenuService.permissionList();
        Map<Integer, List<PermissionBO>> permissionMap = permissionList.stream().collect(Collectors.groupingBy(PermissionBO::getParentId));
        this.buildTree(list, permissionMap);
        return list;
    }

    @Override
    public AdminRoleVO getRole(Integer id) {
        List<AdminRoleVO> roleList = this.getRoleList();
        AdminRoleVO adminRoleVO = roleList.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
        if (adminRoleVO == null) {
            return null;
        }
        if (BooleanUtil.isTrue(adminRoleVO.getIsSystem())) {
            List<Integer> collect = this.getMenuIds(adminMenuService.menuList());
            List<Integer> permissionIdList = adminMenuService.permissionList().stream().map(PermissionBO::getId).collect(Collectors.toList());
            collect.addAll(permissionIdList);
            adminRoleVO.setMenuIds(collect);
        }
        return adminRoleVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveRole(RoleSaveDTO dto) {
        AdminRoleEntity entity = new AdminRoleEntity();
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setDataScope(dto.getDataScope());
        entity.setSort(dto.getSort());
        entity.setIsSystem(false);
        super.save(entity);
        return entity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean editRole(Integer id, RoleSaveDTO dto) {
        AdminRoleEntity entity = super.getById(id);
        if (ObjectUtil.isNotNull(entity)) {
            entity.setName(dto.getName());
            entity.setCode(dto.getCode());
            entity.setDescription(dto.getDescription());
            entity.setDataScope(dto.getDataScope());
            entity.setSort(dto.getSort());
            super.updateById(entity);
        }
        return true;
    }

    @Override
    public Boolean deleteRole(List<Integer> ids) {
        super.removeByIds(ids);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean editRoleMenu(Integer id, List<Integer> menuIds) {
        AdminRoleEntity entity = super.getById(id);
        entity.setMenuJoin(StrUtil.join(",", menuIds));
        super.updateById(entity);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserList(Integer id, List<Integer> userIdList) {
        for (Integer userId : userIdList) {
            AdminUserSimpleVO user = adminUserService.getUser(userId);
            List<Integer> roleIds = user.getRoleIds();
            if (CollUtil.isEmpty(roleIds)) {
                roleIds = new ArrayList<>();
            }
            if (!roleIds.contains(id)) {
                roleIds.add(id);
                adminUserService.editUserRole(userId, roleIds);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteRoleUserRel(Integer id, List<Integer> userIdList) {
        for (Integer userId : userIdList) {
            AdminUserSimpleVO user = adminUserService.getUser(userId);
            List<Integer> roleIds = user.getRoleIds();
            if (CollUtil.isEmpty(roleIds)) {
                roleIds = new ArrayList<>();
            }
            if (roleIds.contains(id)) {
                roleIds.remove(id);
                adminUserService.editUserRole(userId, roleIds);
            }
        }
        return true;
    }

    private List<Integer> getMenuIds(List<MenuInfoBO> infoBOList) {
        List<Integer> menuIds = new ArrayList<>();
        for (MenuInfoBO bo : infoBOList) {
            menuIds.add(bo.getId());
            List<MenuInfoBO> children = bo.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                menuIds.addAll(getMenuIds(children));
            }
        }
        return menuIds;
    }

    private void buildTree(List<RolePermissionVO> list, Map<Integer, List<PermissionBO>> permissionMap) {
        for (RolePermissionVO permissionVO : list) {
            Integer type = permissionVO.getType();
            if (ObjectUtil.equal(type, 2)) {
                List<PermissionBO> permissionBOS = permissionMap.get(permissionVO.getId());
                if (ObjectUtil.isNotEmpty(permissionBOS)) {
                    List<RolePermissionVO> children = new ArrayList<>();
                    for (PermissionBO permissionBO : permissionBOS) {
                        RolePermissionVO child = new RolePermissionVO();
                        child.setId(permissionBO.getId());
                        child.setTitle(permissionBO.getName());
                        child.setPermission(permissionBO.getCode());
                        child.setParentId(permissionBO.getParentId());
                        children.add(child);
                    }
                    permissionVO.setChildren(children);
                }
            } else {
                buildTree(permissionVO.getChildren(), permissionMap);
            }
        }

    }

}
