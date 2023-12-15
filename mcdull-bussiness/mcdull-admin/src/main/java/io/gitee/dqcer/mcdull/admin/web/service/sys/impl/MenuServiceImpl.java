package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.MenuConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuAddDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.MenuLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.MenuDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuTreeVo;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.MenuVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.RoleMenuTreeSelectVO;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IMenuRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.IUserRoleRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IRoleManager;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IMenuService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.TreeUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class MenuServiceImpl extends BasicServiceImpl<IMenuRepository> implements IMenuService {


    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IRoleManager roleManager;


    @Resource
    private IRoleRepository repository;

    @Override
    public Result<List<MenuVO>> list(MenuLiteDTO dto) {
        List<MenuVO> voList = new ArrayList<>();

        boolean admin = UserContextHolder.isAdmin();
        if (admin) {
            List<MenuDO> list = baseRepository.list(dto.getMenuName(), dto.getStatus(), null);
            for (MenuDO menuDO : list) {
                voList.add(MenuConvert.convertToMenuVO(menuDO));
            }
            return Result.success(voList);
        }

        // 只显示当前用户所拥有的菜单
        List<Long> roleByUserId = userRoleRepository.listRoleByUserId(UserContextHolder.currentUserId());
        if (CollUtil.isEmpty(roleByUserId)) {
            return Result.success(voList);
        }
        List<MenuDO> menuByRole = roleManager.getMenuByRole(roleByUserId);
        if (CollUtil.isEmpty(menuByRole)) {
            return Result.success(voList);
        }
        List<MenuDO> list = baseRepository.list(dto.getMenuName(), dto.getStatus(), menuByRole.stream().map(IdDO::getId).collect(Collectors.toList()));
        for (MenuDO menuDO : list) {
            voList.add(MenuConvert.convertToMenuVO(menuDO));
        }
        return Result.success(voList);
    }

    @Override
    public Result<MenuVO> detail(Long id) {
        MenuDO menuDO = baseRepository.getById(id);
        return Result.success(MenuConvert.convertToMenuVO(menuDO));
    }

    @Override
    public Result<RoleMenuTreeSelectVO> roleMenuTreeSelect(Long roleId) {
        List<Long> roles = new ArrayList<>();
        roles.add(roleId);

        List<MenuDO> allmenuList = baseRepository.list();

        RoleMenuTreeSelectVO vo = getMenuTreeSelectVO(allmenuList);

        RoleDO roleDO = repository.getById(roleId);
        List<MenuDO> menus;
        if (UserTypeEnum.READ_WRITE.getCode().equals(roleDO.getType())) {
            menus = roleManager.getMenuByRole(roles);
            vo.setCheckedKeys(menus.stream().map(IdDO::getId).collect(Collectors.toList()));
        }
        return Result.success(vo);
    }

    private RoleMenuTreeSelectVO getMenuTreeSelectVO(List<MenuDO> menus) {
        RoleMenuTreeSelectVO vo = new RoleMenuTreeSelectVO();
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
        return vo;
    }

    @Override
    public Result<RoleMenuTreeSelectVO> treeselect() {
        List<MenuDO> list = baseRepository.list();
        RoleMenuTreeSelectVO vo = this.getMenuTreeSelectVO(list);
        return Result.success(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(MenuAddDTO dto) {
        this.validNameExist(null, dto.getName());
        MenuDO menuDO = MenuConvert.convertDoByDto(dto);
        baseRepository.insert(menuDO);
        return Result.success(menuDO.getId());
    }

    @Override
    protected void validNameExist(Serializable id, String name) {
        List<MenuDO> list = baseRepository.getListByName(name);
        if (null == id) {
            if (CollUtil.isNotEmpty(list)) {
                this.throwDataExistException();
            }
            return;
        }
        long count = list.stream().filter(i -> !Objects.equals(id, i.getId())).count();
        if (!GlobalConstant.Number.NUMBER_0.equals(Convert.toInt(count))) {
            this.throwDataExistException();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(MenuEditDTO dto) {
        Long id = dto.getId();
        this.validNameExist(id, dto.getName());
        if (id.equals(dto.getParentId())) {
            throw new BusinessException("父节点不能是当前本身的节点");
        }
        MenuDO menuDO = MenuConvert.convertDoByDto(dto);
        menuDO.setId(id);
        menuDO.setUpdatedTime(UserContextHolder.getSession().getNow());
        baseRepository.updateById(menuDO);
        return Result.success(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> remove(Long id) {
        List<MenuDO> subMenuList = baseRepository.getSubMenuListByParentId(id);
        if (CollUtil.isNotEmpty(subMenuList)) {
            throw new BusinessException("该节点下存在子节点数据");
        }
        baseRepository.removeById(id);
        return Result.success(id);
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
