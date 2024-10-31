package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IMenuManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuManagerImpl implements IMenuManager {

    @Resource
    private IMenuRepository menuRepository;

    @Override
    public Map<String, MenuEntity> getMenuName(List<String> codeList) {
        if (CollUtil.isNotEmpty(codeList)) {
            return this.listAll().stream()
                    .filter(menuEntity -> codeList.contains(menuEntity.getApiPerms()))
                    .collect(Collectors.toMap(MenuEntity::getApiPerms, Function.identity()));
        }
        return Collections.emptyMap();
    }

    @Override
    public List<MenuEntity> listAll() {
        return menuRepository.list();
    }

}
