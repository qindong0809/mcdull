package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.engine.CompareBean;
import io.gitee.dqcer.mcdull.framework.base.engine.DomainEngine;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleDataScopeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleDataScopeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleDataScopeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleDataScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Service
public class RoleDataScopeServiceImpl extends BasicServiceImpl<IRoleDataScopeRepository> implements IRoleDataScopeService {
    @Override
    public List<RoleDataScopeVO> getListByRole(Long roleId) {
        List<RoleDataScopeVO> voList = new ArrayList<>();
        List<RoleDataScopeEntity> list = baseRepository.getListByRole(roleId);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(entity -> {
                RoleDataScopeVO vo = new RoleDataScopeVO();
                vo.setDataScopeType(entity.getDataScopeType());
                vo.setViewType(entity.getViewType());
                voList.add(vo);
            });
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByRoleId(RoleDataScopeUpdateDTO dto) {
        List<RoleDataScopeUpdateDTO.RoleUpdateDataScopeListFormItem> scopeItemList = dto.getDataScopeItemList();
        Long roleId = dto.getRoleId();
        List<RoleDataScopeEntity> dbList = baseRepository.getListByRole(roleId);
        List<RoleDataScopeEntity> tempList = new ArrayList<>();
        if (CollUtil.isEmpty(scopeItemList)) {
            for (RoleDataScopeUpdateDTO.RoleUpdateDataScopeListFormItem item : scopeItemList) {
                if (CollUtil.isNotEmpty(dbList)) {
                    RoleDataScopeEntity entity = dbList.stream().filter(
                            i -> i.getRoleId().equals(roleId)
                                    && i.getDataScopeType().equals(item.getDataScopeType())
                                    && i.getViewType().equals(item.getViewType())).findFirst().orElse(null);
                    RoleDataScopeEntity temp = new RoleDataScopeEntity();
                    if (ObjUtil.isNotNull(entity)) {
                        temp.setId(entity.getId());
                    }
                    temp.setDataScopeType(item.getDataScopeType());
                    temp.setViewType(item.getViewType());
                    temp.setRoleId(roleId);
                    tempList.add(temp);
                }
            }
        }
        CompareBean<RoleDataScopeEntity, Integer> compare = DomainEngine.compare(dbList, tempList);
        baseRepository.update(compare.getInsertList(), compare.getUpdateList(), compare.getRemoveList());
    }
}
