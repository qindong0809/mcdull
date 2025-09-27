package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptTreeVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminDeptMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminDeptServiceImpl extends ServiceImpl<AdminDeptMapper, AdminDeptEntity> implements IAdminDeptService {


    @Override
    public AdminDeptTreeVO getDeptTree() {
        LambdaQueryWrapper<AdminDeptEntity> queryWrapper = new LambdaQueryWrapper<>();
        List<AdminDeptEntity> list = baseMapper.selectList(queryWrapper);
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

    private AdminDeptTreeVO buildTreeVO(Tree<Integer> tree) {
        AdminDeptTreeVO treeVO = new AdminDeptTreeVO();
        treeVO.setId(Convert.toInt(tree.getId(), 0));
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
