package io.gitee.dqcer.mcdull.admin.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;

import java.util.ArrayList;
import java.util.List;

/**
 * tree 扩展
 *
 * @author dqcer
 * @since 2023/03/26
 */
public class TreeExtensionUtil {

    public static List<TreeSelectVO> convertTreeSelect(List<Tree<Long>> list) {
        List<TreeSelectVO> voList = new ArrayList<>();
        for (Tree<Long> longTree : list) {
            TreeSelectVO vo = new TreeSelectVO();
            vo.setId(longTree.getId());
            vo.setLabel(String.valueOf(longTree.getName()));
            List<Tree<Long>> children = longTree.getChildren();
            if (CollUtil.isNotEmpty(children)) {
                List<TreeSelectVO> subList = convertTreeSelect(children);
                vo.setChildren(subList);
            }
            voList.add(vo);
        }
        return voList;
    }
}
