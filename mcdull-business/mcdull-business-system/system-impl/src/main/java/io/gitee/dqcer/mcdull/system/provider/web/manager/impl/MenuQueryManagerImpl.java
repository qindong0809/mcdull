package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.system.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IMenuQueryManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.MenuMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜单只读查询实现，语义与 MenuServiceImpl 中的原方法保持一致。
 */
@Slf4j
@Service
public class MenuQueryManagerImpl implements IMenuQueryManager {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<String> getCurrentMenuName() {
        UnifySession session = UserContextHolder.getSession();
        String permissionCode = session.getPermissionCode();
        if (StrUtil.isBlank(permissionCode)) {
            throw new BusinessException(I18nConstants.MISSING_PARAMETER);
        }
        List<String> menuNameList = this.getMenuNameByPermissionCode(permissionCode);
        if (CollUtil.isEmpty(menuNameList)) {
            LogHelp.error(log, "menuName is not exist. permissionCode: {}", permissionCode);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        return menuNameList;
    }

    private List<String> getMenuNameByPermissionCode(String permissionCode) {
        List<MenuEntity> all = this.all();
        MenuEntity permissionEntity = all.stream()
                .filter(menuEntity -> StrUtil.equals(permissionCode, menuEntity.getApiPerms()))
                .findFirst().orElse(null);
        if (ObjUtil.isNotNull(permissionEntity)) {
            List<String> nameList = new ArrayList<>();
            this.searchMenuName(all, permissionEntity.getParentId(), nameList);
            return CollUtil.reverse(nameList);
        }
        return Collections.emptyList();
    }

    private void searchMenuName(List<MenuEntity> all, Integer id, List<String> nameList) {
        MenuEntity menu = all.stream()
                .filter(menuEntity -> ObjUtil.equal(id, menuEntity.getId()))
                .findFirst().orElse(null);
        if (ObjUtil.isNotNull(menu)) {
            nameList.add(menu.getMenuName());
            this.searchMenuName(all, menu.getParentId(), nameList);
        }
    }

    private List<MenuEntity> all() {
        LambdaQueryWrapper<MenuEntity> query = Wrappers.lambdaQuery();
        query.eq(BaseEntity::getInactive, InactiveEnum.FALSE.getCode());
        query.orderByAsc(ListUtil.of(MenuEntity::getParentId, MenuEntity::getSort));
        return menuMapper.selectList(query);
    }
}
