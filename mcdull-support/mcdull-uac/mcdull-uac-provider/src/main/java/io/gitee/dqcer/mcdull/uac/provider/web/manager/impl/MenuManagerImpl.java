package io.gitee.dqcer.mcdull.uac.provider.web.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.MenuEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IMenuRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IMenuManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
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
    public List<MenuEntity> listAll() {
        return menuRepository.list();
    }

    @Override
    public List<LabelValueVO<String, String>> getNameCodeList() {
        List<LabelValueVO<String, String>> list = new ArrayList<>();
        List<MenuEntity> listAll = this.listAll();
        Map<String, MenuEntity> collect = this.listAll().stream()
                .filter(menuEntity -> StrUtil.isNotBlank(menuEntity.getApiPerms()))
                .collect(Collectors.toMap(MenuEntity::getApiPerms, Function.identity(), (o1, o2) -> o1));
        for (Map.Entry<String, MenuEntity> entry : collect.entrySet()) {
            MenuEntity menuEntity = entry.getValue();
            if (StrUtil.isNotBlank(menuEntity.getApiPerms())) {
                List<String> rootName = this.getRootName(listAll, menuEntity.getParentId());
                if (CollUtil.isNotEmpty(rootName)) {
                    StringBuilder builder = new StringBuilder();
                    for (String s : rootName) {
                        builder.append(s).append("/");
                    }
                    builder.append(menuEntity.getMenuName());
                    LabelValueVO<String, String> labelValueVO = new LabelValueVO<>(entry.getKey(), builder.toString());
                    list.add(labelValueVO);
                }
            }
        }
        return list;
    }

    public List<String> getRootName(List<MenuEntity> list, Integer id) {
        if (ObjUtil.isNotNull(id)) {
            MenuEntity menuEntity = list.stream().filter(item -> ObjUtil.equal(item.getId(), id)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(menuEntity)) {
                List<String> arrayList = new ArrayList<>();
                arrayList.add(menuEntity.getMenuName());
                List<String> l = this.getRootName(list, menuEntity.getParentId());
                if (CollUtil.isNotEmpty(l)) {
                    arrayList.addAll(l);
                }
                return arrayList;
            }
        }
        return null;
    }

}
