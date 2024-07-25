package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.engine.CompareBean;
import io.gitee.dqcer.mcdull.framework.base.engine.DomainEngine;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.bo.DataScopeBO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleDataScopeUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDataScopeEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.DataScopeTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.DataScopeViewTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DataScopeAndViewTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DataScopeViewTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.RoleDataScopeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IRoleDataScopeRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleDataScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author dqcer
 * @since 2024/05/13
 */
@Service
public class RoleDataScopeServiceImpl
        extends BasicServiceImpl<IRoleDataScopeRepository> implements IRoleDataScopeService {
    @Override
    public List<RoleDataScopeVO> getListByRole(Integer roleId) {
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
        List<RoleDataScopeUpdateDTO.RoleUpdateDataScopeListFormItem> scopeItemList =
                dto.getDataScopeItemList();
        Integer roleId = dto.getRoleId();
        List<RoleDataScopeEntity> dbList = baseRepository.getListByRole(roleId);
        List<RoleDataScopeEntity> tempList = new ArrayList<>();
        if (CollUtil.isNotEmpty(scopeItemList)) {
            for (RoleDataScopeUpdateDTO.RoleUpdateDataScopeListFormItem item : scopeItemList) {
                RoleDataScopeEntity temp = new RoleDataScopeEntity();
                temp.setRoleId(roleId);
                temp.setDataScopeType(item.getDataScopeType());
                temp.setViewType(item.getViewType());
                if (CollUtil.isNotEmpty(dbList)) {
                    RoleDataScopeEntity entity = dbList.stream().filter(
                            i -> i.getRoleId().equals(roleId)
                                    && i.getDataScopeType().equals(item.getDataScopeType())
                                    && i.getViewType().equals(item.getViewType())).findFirst().orElse(null);
                    if (ObjUtil.isNotNull(entity)) {
                        temp.setId(entity.getId());
                    }
                }
                tempList.add(temp);
            }
        }
        CompareBean<RoleDataScopeEntity, Integer> compare = DomainEngine.compare(dbList, tempList);
        baseRepository.update(compare.getInsertList(), compare.getUpdateList(), compare.getRemoveList());
    }

    @Override
    public List<DataScopeAndViewTypeVO> dataScopeList() {
        List<DataScopeBO> dataScopeList = this.getDataScopeType();
        List<DataScopeViewTypeVO> typeList = this.getViewType();
        List<DataScopeAndViewTypeVO> voList = new ArrayList<>();
        for (DataScopeBO bo : dataScopeList) {
            DataScopeAndViewTypeVO vo = new DataScopeAndViewTypeVO();
            vo.setDataScopeType(bo.getDataScopeType());
            vo.setDataScopeTypeName(bo.getDataScopeTypeName());
            vo.setDataScopeTypeDesc(bo.getDataScopeTypeDesc());
            vo.setDataScopeTypeSort(bo.getDataScopeTypeSort());
            vo.setViewTypeList(typeList);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 获取当前系统存在的数据可见范围
     */
    public List<DataScopeViewTypeVO> getViewType() {
        List<DataScopeViewTypeVO> viewTypeList = new ArrayList<>();
        DataScopeViewTypeEnum[] enums = DataScopeViewTypeEnum.class.getEnumConstants();
        DataScopeViewTypeVO dataScopeViewTypeDTO;
        for (DataScopeViewTypeEnum viewTypeEnum : enums) {
            dataScopeViewTypeDTO = DataScopeViewTypeVO
                    .builder()
                    .viewType(viewTypeEnum.getCode())
                    .viewTypeLevel(viewTypeEnum.getLevel())
                    .viewTypeName(viewTypeEnum.getText())
                    .build();
            viewTypeList.add(dataScopeViewTypeDTO);
        }
        Comparator<DataScopeViewTypeVO> comparator = Comparator
                .comparing(DataScopeViewTypeVO::getViewTypeLevel);
        viewTypeList.sort(comparator);
        return viewTypeList;
    }

    public List<DataScopeBO> getDataScopeType() {
        List<DataScopeBO> dataScopeTypeList = new ArrayList<>();

        DataScopeTypeEnum[] enums = DataScopeTypeEnum.class.getEnumConstants();
        DataScopeBO dataScopeDTO;
        for (DataScopeTypeEnum typeEnum : enums) {
            dataScopeDTO = DataScopeBO
                    .builder()
                    .dataScopeType(typeEnum.getCode())
                    .dataScopeTypeDesc(typeEnum.getText())
                    .dataScopeTypeName(typeEnum.getName())
                    .dataScopeTypeSort(typeEnum.getSort())
                    .build();
            dataScopeTypeList.add(dataScopeDTO);
        }
        Comparator<DataScopeBO> comparator = Comparator
                .comparing(DataScopeBO::getDataScopeTypeSort);
        dataScopeTypeList.sort(comparator);
        return dataScopeTypeList;
    }

}
