package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    @Override
    public List<DepartmentVO> getAll() {
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

    @Override
    public List<DepartmentTreeVO> departmentTree() {
        List<DepartmentEntity> list = baseRepository.all();
        if (CollUtil.isNotEmpty(list)) {
            for (DepartmentEntity dept : list) {
                DepartmentTreeVO vo = new DepartmentTreeVO();
                vo.setDepartmentId(Convert.toInt(dept.getId()));
                vo.setName(dept.getName());
                vo.setManagerId(dept.getManagerId());
                vo.setParentId(Convert.toInt(dept.getParentId()));
            }
            List<Tree<Integer>> build = TreeUtil.build(list, 0, (deptDO, treeNode) -> {
                treeNode.setId(Convert.toInt(deptDO.getId()));
                treeNode.setParentId(Convert.toInt(deptDO.getParentId()));
                treeNode.setName(deptDO.getName());
            });
            return convertTreeSelect(build);
        }
        return Collections.emptyList();
    }


    public static List<DepartmentTreeVO> convertTreeSelect(List<Tree<Integer>> list) {
        List<DepartmentTreeVO> voList = new ArrayList<>();
        for (Tree<Integer> longTree : list) {
            DepartmentTreeVO vo = new DepartmentTreeVO();
            vo.setDepartmentId(longTree.getId());
            vo.setName(String.valueOf(longTree.getName()));
            List<Integer> subIdList = new ArrayList<>();
            subIdList.add(longTree.getId());
            List<Tree<Integer>> children = longTree.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                List<DepartmentTreeVO> subList = convertTreeSelect(children);
                vo.setChildren(subList);
                List<Integer> sub = getSubList(subList);
                subIdList.addAll(sub);
            }
            vo.setSelfAndAllChildrenIdList(subIdList);
            voList.add(vo);
        }
        return voList;
    }

    private static List<Integer> getSubList(List<DepartmentTreeVO> subList) {
        List<Integer> subIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(subList)) {
            for (DepartmentTreeVO treeVO : subList) {
                subIdList.add(treeVO.getDepartmentId());
                if (CollUtil.isNotEmpty(treeVO.getChildren())) {
                    List<Integer> sub = getSubList(treeVO.getChildren());
                    subIdList.addAll(sub);
                }
            }
        }
        return subIdList;
    }

    private DepartmentVO convertToVO(DepartmentEntity dept) {
        DepartmentVO vo = new DepartmentVO();
        vo.setDepartmentId(Convert.toInt(dept.getId()));
        vo.setName(dept.getName());
        vo.setManagerId(dept.getManagerId());
        vo.setParentId(Convert.toInt(dept.getParentId()));
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
