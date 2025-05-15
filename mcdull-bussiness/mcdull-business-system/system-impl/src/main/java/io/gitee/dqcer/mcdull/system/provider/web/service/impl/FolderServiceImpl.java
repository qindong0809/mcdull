package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.audit.FolderAudit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FolderInsertDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FolderUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FolderEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FolderInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FolderTreeInfoVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFolderRepository;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFolderService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IMenuService;
import jakarta.annotation.Resource;
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
public class FolderServiceImpl
        extends BasicServiceImpl<IFolderRepository>  implements IFolderService {

    public static final String SYSTEM_FOLDER = "系统文件";

//    @Resource
//    private IAuditManager auditManager;
    @Resource
    private IMenuService menuService;

    @Override
    public List<FolderInfoVO> getAll() {
        List<FolderInfoVO> list = new ArrayList<>();
        List<FolderEntity> entityList = baseRepository.list();
        if (CollUtil.isNotEmpty(entityList)) {
            for (FolderEntity folder : entityList) {
                FolderInfoVO vo = this.convertToVO(folder);
                list.add(vo);
            }
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer getSystemFolderId() {
        List<String> menuNameList = menuService.getCurrentMenuName();
        FolderEntity entity = baseRepository.getSystemFolderId(0, SYSTEM_FOLDER);
        Integer systemId;
        if (ObjUtil.isNull(entity)) {
            FolderInsertDTO dto = new FolderInsertDTO();
            dto.setName(SYSTEM_FOLDER);
            dto.setParentId(0);
            systemId = this.insert(dto, false);
        } else {
            systemId = entity.getId();
        }
        Integer folderId = systemId;
        for (String menuName : menuNameList) {
            FolderEntity menuFolder = baseRepository.getSystemFolderId(folderId, menuName);
            if (ObjUtil.isNull(menuFolder)) {
                FolderInsertDTO dto = new FolderInsertDTO();
                dto.setName(menuName);
                dto.setParentId(folderId);
                folderId = this.insert(dto, false);
            } else {
                folderId = menuFolder.getId();
            }
        }
        return folderId;
    }

    @Override
    public String getRootToNodeName(Integer folderId) {
        FolderEntity folder = baseRepository.getById(folderId);
        if (ObjUtil.isNotNull(folder)) {
            String idPath = folder.getIdPath();
            List<String> split = StrUtil.split(idPath, StrUtil.SLASH);
            List<Integer> list = Convert.toList(Integer.class, split);
            List<FolderEntity> folderEntities = baseRepository.listByIds(list);
            if (CollUtil.isNotEmpty(folderEntities)) {
                folderEntities.sort(Comparator.comparingInt(FolderEntity::getParentId));
                return folderEntities.stream().map(FolderEntity::getName).collect(Collectors.joining(StrUtil.SLASH));
            }
        }
        return StrUtil.EMPTY;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addIfAbsent(String name, Integer parentId) {
        List<FolderEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            FolderEntity folder = childList.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
            if (ObjUtil.isNotNull(folder)) {
                return folder.getId();
            }
        }
        FolderInsertDTO dto = new FolderInsertDTO();
        dto.setName(name);
        dto.setParentId(parentId);
        return this.insert(dto, false);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insert(FolderInsertDTO dto, boolean isSaveLog) {
        Integer parentId = dto.getParentId();
        List<FolderEntity> childList = baseRepository.listByParentId(parentId);
        int sort = 1;
        if (CollUtil.isNotEmpty(childList)) {
            boolean anyMatch = childList.stream().anyMatch(i -> i.getName().equals(dto.getName()));
            if (anyMatch) {
                throw new BusinessException(I18nConstants.NAME_DUPLICATED);
            }
            sort = childList.stream().max(Comparator.comparingInt(FolderEntity::getSort))
                    .map(FolderEntity::getSort).orElse(0) + 1;
        }
        FolderEntity folder = this.convertToEntity(dto);
        List<FolderEntity> list = baseRepository.all();
        List<Integer> integers = this.getParentIds(list, parentId, new ArrayList<>());
        List<Integer> reverse = CollUtil.reverse(integers);
        folder.setIdPath(reverse.stream().map(Convert::toStr).collect(Collectors.joining(StrUtil.SLASH)));
        folder.setSort(sort);
        baseRepository.save(folder);
        if (isSaveLog) {
//            auditManager.saveByAddEnum(dto.getName(), menu.getId(), this.buildAuditLog(menu));
        }
        return folder.getId();
    }

    private List<Integer> getParentIds(List<FolderEntity> all, Integer parentId, List<Integer> parentIds) {
        parentIds.add(parentId);
        FolderEntity folderEntity = all.stream().filter(i -> i.getId().equals(parentId)).findFirst().orElse(null);
        if (ObjUtil.isNotNull(folderEntity)) {
            Integer pId = folderEntity.getParentId();
            if (ObjUtil.isNotNull(pId)) {
                getParentIds(all, pId, parentIds);
            }
        }
        return parentIds;
    }

    private Audit buildAuditLog(FolderEntity menu) {
        FolderAudit audit = new FolderAudit();
        audit.setName(menu.getName());
        audit.setSort(menu.getSort());
        Integer parentId = menu.getParentId();
        if (ObjUtil.isNotNull(parentId)) {
            FolderEntity department = baseRepository.getById(parentId);
            if (ObjUtil.isNotNull(department)) {
                audit.setParentName(department.getName());
            }
        }
        return audit;
    }

    private FolderEntity convertToEntity(FolderInsertDTO dto) {
        FolderEntity entity = new FolderEntity();
        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        return entity;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(Integer id, FolderUpdateDTO dto) {
        FolderEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        Integer parentId = dto.getParentId();
        List<FolderEntity> childList = baseRepository.listByParentId(parentId);
        if (CollUtil.isNotEmpty(childList)) {
            this.validNameExist(id, dto.getName(), childList,
                    i -> !i.getId().equals(id) && i.getName().equals(dto.getName()));
        }
        this.settingUpdateValue(dto, entity);
        baseRepository.updateById(entity);
//        auditManager.saveByUpdateEnum(dto.getName(), id,
//                this.buildAuditLog(entity), this.buildAuditLog(baseRepository.getById(id)));
        return true;
    }

    private void settingUpdateValue(FolderUpdateDTO dto, FolderEntity entity) {
        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Integer id) {
        FolderEntity department = baseRepository.getById(id);
        if (ObjUtil.isNull(department)) {
            this.throwDataNotExistException(id);
        }
        List<FolderEntity> all = baseRepository.list();
        List<FolderEntity> currentList = this.getChildNodeByParentId(all, id);
        if (CollUtil.isNotEmpty(currentList)) {
            List<Integer> deptIdList = currentList.stream().map(FolderEntity::getId).collect(Collectors.toList());
//            List<UserEntity> userList =userService.listByDeptList(deptIdList);
//            if (CollUtil.isNotEmpty(userList)) {
//                LogHelp.error(log, "exist data. {}", () ->
//                        userList.stream().map(UserEntity::getLoginName).collect(Collectors.toList()));
//                throw new BusinessException("dept.has.user");
//            }
        }
        baseRepository.removeById(id);
//        auditManager.saveByDeleteEnum(department.getName(), department.getId(), "");
        return true;
    }

    @Override
    public List<FolderTreeInfoVO> getTree() {
        List<FolderEntity> list = baseRepository.all();
        // todo FileFolderTypeEnum
        if (CollUtil.isNotEmpty(list)) {
            List<Tree<Integer>> build = TreeUtil.build(list, 0, (entity, treeNode) -> {
                treeNode.setId(Convert.toInt(entity.getId()));
                treeNode.setParentId(Convert.toInt(entity.getParentId()));
                treeNode.setName(entity.getName());
            });
            return convertTreeSelect(build);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<Integer, String> getMap(Set<Integer> folderSet) {
        if (CollUtil.isNotEmpty(folderSet)) {
            List<FolderEntity> folderEntities = baseRepository.listByIds(folderSet);
            if (CollUtil.isNotEmpty(folderEntities)) {
                return folderEntities.stream().collect(Collectors.toMap(FolderEntity::getId, FolderEntity::getName));
            }
        }
        return Map.of();
    }

    private List<FolderTreeInfoVO> convertTreeSelect(List<Tree<Integer>> list) {
        List<FolderTreeInfoVO> voList = new ArrayList<>();
        for (Tree<Integer> longTree : list) {
            FolderTreeInfoVO vo = new FolderTreeInfoVO();
            vo.setId(longTree.getId());
            vo.setKey(longTree.getId() + "");
            vo.setTitle(String.valueOf(longTree.getName()));
            vo.setName(String.valueOf(longTree.getName()));
            List<Integer> subIdList = new ArrayList<>();
            subIdList.add(longTree.getId());
            List<Tree<Integer>> children = longTree.getChildren();
            vo.setIsLeaf(CollUtil.isEmpty(children));
            if (CollUtil.isNotEmpty(children)) {
                List<FolderTreeInfoVO> subList = convertTreeSelect(children);
                vo.setChildren(subList);
//                List<Integer> sub = getSubList(subList);
//                subIdList.addAll(sub);
            }
            vo.setSelfAndAllChildrenIdList(subIdList);
            voList.add(vo);
        }
        return voList;
    }

    private List<FolderEntity> getChildNodeByParentId(List<FolderEntity> all, Integer parentId) {
        List<FolderEntity> list = all.stream()
                .filter(i->i.getParentId().equals(parentId)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            for (FolderEntity entity : list) {
                List<FolderEntity> childNodeList = getChildNodeByParentId(all, entity.getId());
                if (CollUtil.isNotEmpty(childNodeList)) {
                    list.addAll(childNodeList);
                }
            }
        }
        return list;
    }

    private FolderInfoVO convertToVO(FolderEntity entity) {
        FolderInfoVO vo = new FolderInfoVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setParentId(entity.getParentId());
        vo.setSort(entity.getSort());
        vo.setCreateTime(entity.getCreatedTime());
        vo.setUpdateTime(entity.getUpdatedTime());
        return vo;
    }
}
