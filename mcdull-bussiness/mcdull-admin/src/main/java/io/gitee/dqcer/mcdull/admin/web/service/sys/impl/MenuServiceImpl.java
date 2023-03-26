package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.MenuConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuTreeVo;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleMenuTreeSelectVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IMenuService;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.TreeUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private IMenuRepository menuRepository;

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IRoleManager roleManager;

    @Override
    public Result<List<MenuVO>> list(MenuLiteDTO dto) {
        List<MenuVO> voList = new ArrayList<>();

        boolean admin = UserContextHolder.isAdmin();
        if (admin) {
            List<MenuDO> list = menuRepository.list(dto.getMenuName(), dto.getStatus(), null);
            for (MenuDO menuDO : list) {
                voList.add(MenuConvert.convertToMenuVO(menuDO));
            }
            return Result.ok(voList);
        }

        // 只显示当前用户所拥有的菜单
        List<Long> roleByUserId = userRoleRepository.listRoleByUserId(UserContextHolder.getSession().getUserId());
        if (CollUtil.isEmpty(roleByUserId)) {
            return Result.ok(voList);
        }
        List<MenuDO> menuByRole = roleManager.getMenuByRole(roleByUserId);
        if (CollUtil.isEmpty(menuByRole)) {
            return Result.ok(voList);
        }
        List<MenuDO> list = menuRepository.list(dto.getMenuName(), dto.getStatus(), menuByRole.stream().map(IdDO::getId).collect(Collectors.toList()));
        for (MenuDO menuDO : list) {
            voList.add(MenuConvert.convertToMenuVO(menuDO));
        }
        return Result.ok(voList);
    }

    @Override
    public Result<MenuVO> detail(Long id) {
        MenuDO menuDO = menuRepository.getById(id);
        return Result.ok(MenuConvert.convertToMenuVO(menuDO));
    }

    @Override
    public Result<RoleMenuTreeSelectVO> roleMenuTreeSelect(Long roleId) {
        RoleMenuTreeSelectVO vo = new RoleMenuTreeSelectVO();
        List<Long> roles = new ArrayList<>();
        roles.add(roleId);
        List<MenuDO> menus = roleManager.getMenuByRole(roles);
        if (CollUtil.isNotEmpty(menus)) {
            List<Long> menuIds = menus.stream().map(IdDO::getId).collect(Collectors.toList());
            vo.setCheckedKeys(menuIds);

            List<MenuTreeVo> treeVoList = new ArrayList<>();
            for (MenuDO menu : menus) {
                treeVoList.add(MenuConvert.convertMenuTreeVo(menu));
            }

            // tree vo
            List<MenuTreeVo> treeObjects = TreeUtil.getChildTreeObjects(treeVoList, 0L);

            List<TreeSelectVO> treeSelectVOS = this.getTreeSelectVO(treeObjects);

            vo.setMenus(treeSelectVOS);
        }
        return Result.ok(vo);
    }

    private List<TreeSelectVO> getTreeSelectVO(List<MenuTreeVo> treeObjects) {
        List<TreeSelectVO> treeSelectVO = new ArrayList<>();
        for (MenuTreeVo treeVo : treeObjects) {
            TreeSelectVO selectVO = new TreeSelectVO();
            selectVO.setId(treeVo.getId());
            selectVO.setLabel(treeVo.getName());
            List<MenuTreeVo> children = treeVo.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                List<TreeSelectVO> childrenList = this.getTreeSelectVO(children);
                selectVO.setChildren(childrenList);
            }
            treeSelectVO.add(selectVO);
        }
        return treeSelectVO;
    }
}
