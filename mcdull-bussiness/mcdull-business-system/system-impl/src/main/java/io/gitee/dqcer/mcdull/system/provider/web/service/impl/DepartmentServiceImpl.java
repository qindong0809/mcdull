package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.audit.DepartmentAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DeptInsertDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DeptUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DepartmentTreeInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDepartmentService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Department Service Impl
 *
 * @author dqcer
 * @since 2024/7/25 10:39
 */
@Service
public class DepartmentServiceImpl
        extends BasicServiceImpl<IDepartmentRepository>  implements IDepartmentService {

    @Resource
    private IUserService userService;

    @Resource
    private IAuditManager auditManager;

//    @Cacheable(cacheNames = "caffeineCache", key = "'department-all'")
    @Override
    public List<DepartmentInfoVO> getAll() {
        List<DepartmentInfoVO> list = new ArrayList<>();
        List<DepartmentEntity> deptList = baseRepository.list();
        if (CollUtil.isNotEmpty(deptList)) {
            for (DepartmentEntity dept : deptList) {
                DepartmentInfoVO vo = this.convertToVO(dept);
                list.add(vo);
            }
            Set<Integer> userSet = deptList.stream()
                    .map(DepartmentEntity::getManagerId)
                    .filter(ObjUtil::isNotNull)
                    .collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(userSet)) {
                Map<Integer, String> userMap = userService.getNameMap(new ArrayList<>(userSet));
                for (DepartmentInfoVO vo : list) {
                    vo.setManagerName(userMap.get(vo.getManagerId()));
                }
            }
        }
        return list;
    }


    @CacheEvict(cacheNames = "caffeineCache", key = "'department-all'", allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insert(DeptInsertDTO dto) {
        Integer parentId = dto.getParentId();
        List<DepartmentEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
        }
        DepartmentEntity menu = this.convertToEntity(dto);
        baseRepository.save(menu);
        auditManager.saveByAddEnum(dto.getName(), menu.getId(), this.buildAuditLog(menu));
        return true;
    }

    private Audit buildAuditLog(DepartmentEntity menu) {
        DepartmentAudit audit = new DepartmentAudit();
        audit.setName(menu.getName());
        audit.setSort(menu.getSort());
        Integer managerId = menu.getManagerId();
        if (ObjUtil.isNotNull(managerId)) {
            UserEntity user = userService.get(managerId);
            if (ObjUtil.isNotNull(user)) {
                audit.setManagerName(user.getActualName());
            }
        }
        Integer parentId = menu.getParentId();
        if (ObjUtil.isNotNull(parentId)) {
            DepartmentEntity department = this.getById(parentId);
            if (ObjUtil.isNotNull(department)) {
                audit.setParentName(department.getName());
            }
        }
        return audit;
    }

    private DepartmentEntity convertToEntity(DeptInsertDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(dto.getName());
        entity.setManagerId(entity.getManagerId());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        return entity;

    }

    @CacheEvict(cacheNames = "caffeineCache", key = "'department-all'", allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, DeptUpdateDTO dto) {
        DepartmentEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        Integer parentId = dto.getParentId();
        List<DepartmentEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            this.validNameExist(id, dto.getName(), childList,
                    i -> !i.getId().equals(id) && i.getName().equals(dto.getName()));
        }
        this.settingUpdateValue(dto, entity);
        baseRepository.updateById(entity);
        auditManager.saveByUpdateEnum(dto.getName(), id,
                this.buildAuditLog(entity), this.buildAuditLog(getById(id)));
        return true;
    }

    private void settingUpdateValue(DeptUpdateDTO dto, DepartmentEntity entity) {
        entity.setName(dto.getName());
        entity.setManagerId(dto.getManagerId());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
    }

    @CacheEvict(cacheNames = "caffeineCache", key = "'department-all'", allEntries=true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id) {
        DepartmentEntity department = baseRepository.getById(id);
        if (ObjUtil.isNull(department)) {
            this.throwDataNotExistException(id);
        }
        List<DepartmentEntity> all = baseRepository.list();
        List<DepartmentEntity> currentList = this.getChildNodeByParentId(all, id);
        if (CollUtil.isNotEmpty(currentList)) {
            List<Integer> deptIdList = currentList.stream().map(DepartmentEntity::getId).collect(Collectors.toList());
            List<UserEntity> userList =userService.listByDeptList(deptIdList);
            if (CollUtil.isNotEmpty(userList)) {
                LogHelp.error(log, "exist data. {}", () ->
                        userList.stream().map(UserEntity::getLoginName).collect(Collectors.toList()));
                throw new BusinessException("dept.has.user");
            }
        }
        baseRepository.removeById(id);
        auditManager.saveByDeleteEnum(department.getName(), department.getId(), "");
        return true;
    }

    private List<DepartmentEntity> getChildNodeByParentId(List<DepartmentEntity> all, Integer parentId) {
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
    public List<DepartmentTreeInfoVO> departmentTree() {
        List<DepartmentEntity> list = baseRepository.all();
        if (CollUtil.isNotEmpty(list)) {
            List<Tree<Integer>> build = TreeUtil.build(list, 0, (deptDO, treeNode) -> {
                treeNode.setId(Convert.toInt(deptDO.getId()));
                treeNode.setParentId(Convert.toInt(deptDO.getParentId()));
                treeNode.setName(deptDO.getName());
            });
            return convertTreeSelect(build);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Integer, String> getNameMap(List<Integer> idList) {
        List<DepartmentEntity> list = baseRepository.listByIds(idList);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream()
                    .collect(Collectors.toMap(DepartmentEntity::getId, DepartmentEntity::getName));
        }
        return Collections.emptyMap();
    }

    @Override
    public List<Integer> getChildrenIdList(Integer departmentId) {
        List<Integer> voList = new ArrayList<>();
        List<DepartmentEntity> list = baseRepository.listByParentId(departmentId);
        if (CollUtil.isNotEmpty(list)) {
            List<Integer> idList = list.stream()
                    .map(DepartmentEntity::getId).collect(Collectors.toList());
            voList.addAll(idList);
            for (Integer id : idList) {
                List<Integer> childrenIdList = this.getChildrenIdList(id);
                if (CollUtil.isNotEmpty(childrenIdList)) {
                    voList.addAll(childrenIdList);
                }
            }
        }
        return voList;
    }

    @Override
    public DepartmentEntity getById(Integer departmentId) {
        return baseRepository.getById(departmentId);
    }


    public static List<DepartmentTreeInfoVO> convertTreeSelect(List<Tree<Integer>> list) {
        List<DepartmentTreeInfoVO> voList = new ArrayList<>();
        for (Tree<Integer> longTree : list) {
            DepartmentTreeInfoVO vo = new DepartmentTreeInfoVO();
            vo.setDepartmentId(longTree.getId());
            vo.setName(String.valueOf(longTree.getName()));
            List<Integer> subIdList = new ArrayList<>();
            subIdList.add(longTree.getId());
            List<Tree<Integer>> children = longTree.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                List<DepartmentTreeInfoVO> subList = convertTreeSelect(children);
                vo.setChildren(subList);
                List<Integer> sub = getSubList(subList);
                subIdList.addAll(sub);
            }
            vo.setSelfAndAllChildrenIdList(subIdList);
            voList.add(vo);
        }
        return voList;
    }

    private static List<Integer> getSubList(List<DepartmentTreeInfoVO> subList) {
        List<Integer> subIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(subList)) {
            for (DepartmentTreeInfoVO treeVO : subList) {
                subIdList.add(treeVO.getDepartmentId());
                if (CollUtil.isNotEmpty(treeVO.getChildren())) {
                    List<Integer> sub = getSubList(treeVO.getChildren());
                    subIdList.addAll(sub);
                }
            }
        }
        return subIdList;
    }

    private DepartmentInfoVO convertToVO(DepartmentEntity dept) {
        DepartmentInfoVO vo = new DepartmentInfoVO();
        vo.setDepartmentId(Convert.toInt(dept.getId()));
        vo.setName(dept.getName());
        vo.setManagerId(dept.getManagerId());
        vo.setParentId(Convert.toInt(dept.getParentId()));
        vo.setSort(dept.getSort());
        vo.setCreateTime(dept.getCreatedTime());
        vo.setUpdateTime(dept.getUpdatedTime());
        return vo;

    }
}
