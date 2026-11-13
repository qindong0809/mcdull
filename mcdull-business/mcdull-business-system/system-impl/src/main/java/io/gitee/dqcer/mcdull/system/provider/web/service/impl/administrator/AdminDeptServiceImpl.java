package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.DeptSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptInfoVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptListTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptTreeVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminDeptMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminDeptServiceImpl extends ServiceImpl<AdminDeptMapper, AdminDeptEntity> implements IAdminDeptService {

    @Resource
    private IAdminUserService adminUserService;


    @Override
    public AdminDeptTreeVO getDeptTree() {
        List<AdminDeptEntity> list = getList();
        Tree<Integer> build = TreeUtil.buildSingle(list, 0, (e, v) -> {
            v.setId(e.getId());
            v.setParentId(e.getParentId());
            v.setName(e.getName());
            v.setWeight(e.getSort());
            v.putExtra("sort", e.getSort());
        });
        Tree<Integer> root = build.getChildren().get(0);
        return this.buildTreeVO(root);
    }

    private List<AdminDeptEntity> getList() {
        LambdaQueryWrapper<AdminDeptEntity> queryWrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public AdminDeptListTreeVO getDeptListTree() {
        List<AdminDeptEntity> list = getList();
        Map<Integer, String> userNameMap = adminUserService.getUserNameMap();
        Tree<Integer> build = TreeUtil.buildSingle(list, 0, (e, v) -> {
            v.setId(e.getId());
            v.setParentId(e.getParentId());
            v.setName(e.getName());
            v.setWeight(e.getSort());
            v.putExtra("sort", e.getSort());
            v.putExtra("isSystem", e.getIsSystem());
            v.putExtra("createTime", e.getCreatedTime());
            v.putExtra("updateTime", e.getUpdatedTime());
            v.putExtra("createUser", e.getCreatedBy());
            v.putExtra("updateUser", e.getUpdatedBy());
            v.putExtra("createUserString", userNameMap.getOrDefault(e.getCreatedBy(), StrUtil.EMPTY));
            v.putExtra("updateUserString", userNameMap.getOrDefault(e.getUpdatedBy(), StrUtil.EMPTY));
            v.putExtra("inactive", e.getInactive());
            v.putExtra("description", Convert.toStr(e.getDescription(), StrUtil.EMPTY));
            v.putExtra("status", e.getStatus() ? 1 : 2);
        });
        Tree<Integer> root = build.getChildren().get(0);
        return this.buildListTreeVO(root);
    }

    @Override
    public Map<Integer, String> getDeptNameMap() {
        List<AdminDeptEntity> list = super.list();
        return list.stream().collect(Collectors.toMap(AdminDeptEntity::getId, AdminDeptEntity::getName));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer saveDept(DeptSaveDTO dto) {
        AdminDeptEntity entity = new AdminDeptEntity();
        entity.setName(dto.getName());
        entity.setParentId(dto.getParentId());
        entity.setSort(dto.getSort());
        entity.setStatus(ObjectUtil.equal(dto.getStatus(), 1));
        entity.setDescription(dto.getDescription());
        baseMapper.insert(entity);
        return entity.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deleteDept(List<Integer> idList) {
        baseMapper.deleteByIds(idList);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminDeptInfoVO get(Integer id) {
        AdminDeptEntity adminDeptEntity = baseMapper.selectById(id);
        AdminDeptInfoVO infoVO = new AdminDeptInfoVO();
        infoVO.setId(adminDeptEntity.getId());
        infoVO.setParentId(adminDeptEntity.getParentId());
        infoVO.setName(adminDeptEntity.getName());
        infoVO.setSort(adminDeptEntity.getSort());
        infoVO.setStatus(adminDeptEntity.getStatus() ? 1 : 2);
        infoVO.setDescription(adminDeptEntity.getDescription());
        return infoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean editDept(Integer id, DeptSaveDTO dto) {
        AdminDeptEntity entity = baseMapper.selectById(id);
        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setStatus(ObjectUtil.equal(dto.getStatus(), 1));
        entity.setDescription(dto.getDescription());
        baseMapper.updateById(entity);
        return true;
    }

    @Override
    public void exportDept() {
        // todo export
    }

    private AdminDeptListTreeVO buildListTreeVO(Tree<Integer> tree) {
        AdminDeptListTreeVO treeVO = new AdminDeptListTreeVO();
        treeVO.setId(Convert.toInt(tree.getId(), 0));
        treeVO.setParentId(Convert.toInt(tree.getParentId()));
        treeVO.setName(String.valueOf(tree.getName()));
        treeVO.setSort(Convert.toInt(tree.get("sort")));
        treeVO.setIsSystem(Convert.toBool(tree.get("isSystem")));
        treeVO.setCreateTime(Convert.toDate(tree.get("createTime")));
        treeVO.setUpdateTime(Convert.toDate(tree.get("updateTime")));
        treeVO.setCreateUserString(String.valueOf(tree.get("createUserString")));
        treeVO.setUpdateUserString(String.valueOf(tree.get("updateUserString")));
        treeVO.setCreateUser(Convert.toInt(tree.get("createUser")));
        treeVO.setUpdateUser(Convert.toInt(tree.get("updateUser")));
        treeVO.setDescription(String.valueOf(tree.get("description")));
        treeVO.setStatus(Convert.toInt(tree.get("status")));
        List<AdminDeptListTreeVO> childVOList = new ArrayList<>();
        List<Tree<Integer>> children = tree.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Tree<Integer> child : children) {
                childVOList.add(buildListTreeVO(child));
            }
            treeVO.setChildren(childVOList);
        }
        return treeVO;
    }

    private AdminDeptTreeVO buildTreeVO(Tree<Integer> tree) {
        AdminDeptTreeVO treeVO = new AdminDeptTreeVO();
        treeVO.setKey(Convert.toInt(tree.getId(), 0));
        treeVO.setParentId(Convert.toInt(tree.getParentId()));
        treeVO.setTitle(String.valueOf(tree.getName()));
        treeVO.setSort(Convert.toInt(tree.get("sort")));
        List<AdminDeptTreeVO> childVOList = new ArrayList<>();
        List<Tree<Integer>> children = tree.getChildren();
        if (children != null && !children.isEmpty()) {
            for (Tree<Integer> child : children) {
                childVOList.add(buildTreeVO(child));
            }
            treeVO.setChildren(childVOList);
        }
        return treeVO;
    }
}
