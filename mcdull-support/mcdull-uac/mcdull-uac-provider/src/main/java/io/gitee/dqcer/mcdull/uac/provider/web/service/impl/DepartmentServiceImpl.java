package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dqcer
 */
@Service
public class DepartmentServiceImpl extends BasicServiceImpl<IDepartmentRepository>  implements IDepartmentService {

    @Override
    public List<DepartmentVO> list(DeptListDTO dto) {
        List<DepartmentVO> list = new ArrayList<>();
        List<DepartmentEntity> deptList = baseRepository.list();
        if (CollUtil.isNotEmpty(deptList)) {
            for (DepartmentEntity dept : deptList) {
                DepartmentVO vo = this.convertToVO(dept);
                list.add(vo);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(DeptInsertDTO dto) {
        Long parentId = dto.getParentId();
        List<DepartmentEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        DepartmentEntity menu = this.convertToEntity(dto);
        return baseRepository.save(menu);
    }

    private DepartmentEntity convertToEntity(DeptInsertDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(dto.getName());
        entity.setManagerId(entity.getManagerId());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        return entity;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Long id, DeptUpdateDTO dto) {
        Long parentId = dto.getParentId();
        List<DepartmentEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream()
                    .anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        DepartmentEntity menu = this.convertToEntity(dto);
        menu.setId(id);
        return baseRepository.updateById(menu);
    }

    private DepartmentEntity convertToEntity(DeptUpdateDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(dto.getName());
        entity.setManagerId(dto.getManagerId());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id, ReasonDTO dto) {
        return baseRepository.delete(id, dto.getReason());
    }

    private DepartmentVO convertToVO(DepartmentEntity dept) {
        DepartmentVO vo = new DepartmentVO();
        vo.setId(dept.getId());
        vo.setName(dept.getName());
        vo.setManagerId(dept.getManagerId());
        vo.setParentId(dept.getParentId());
        vo.setSort(dept.getSort());
        return vo;

    }

    private Integer getStatus(Boolean inactive) {
        return BooleanUtil.toInteger(!inactive);
    }

    private Boolean getInactive(Integer status) {
        return !BooleanUtil.toBooleanObject(Convert.toStr(status));
    }


}
