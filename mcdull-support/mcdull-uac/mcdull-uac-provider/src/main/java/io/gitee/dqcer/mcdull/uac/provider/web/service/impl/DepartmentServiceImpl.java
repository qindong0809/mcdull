package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentTreeVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DepartmentVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dqcer
 */
@Service
public class DepartmentServiceImpl extends BasicServiceImpl<IDepartmentRepository>  implements IDepartmentService {

    @Resource
    private IUserService userService;

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
        DepartmentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        Long parentId = dto.getParentId();
        List<DepartmentEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            this.validNameExist(id, dto.getName(), childList,
                    i -> !i.getId().equals(id) && i.getName().equals(dto.getName()));
        }
        this.settingUpdateValue(dto, entity);
        return baseRepository.updateById(entity);
    }

    private void settingUpdateValue(DeptUpdateDTO dto, DepartmentEntity entity) {
        entity.setName(dto.getName());
        entity.setManagerId(dto.getManagerId());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Long id) {
        List<DepartmentEntity> all = baseRepository.list();
        List<DepartmentEntity> currentList = this.getChildNodeByParentId(all, id);
        if (CollUtil.isNotEmpty(currentList)) {
            List<Long> deptIdList = currentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
            List<UserEntity> userList =userService.listByDeptList(deptIdList);
            if (CollUtil.isNotEmpty(userList)) {
                throw new BusinessException("dept.has.user");
            }
        }
        return baseRepository.removeById(id);
    }

    private List<DepartmentEntity> getChildNodeByParentId(List<DepartmentEntity> all, Long parentId) {
        List<DepartmentEntity> list = all.stream()
                .filter(i->i.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            for (DepartmentEntity entity : list) {
                List<DepartmentEntity> childNodeList = getChildNodeByParentId(all, entity.getId());
                if (CollUtil.isNotEmpty(childNodeList)) {
                    list.addAll(childNodeList);
                }
            }
        }
        return list;
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
