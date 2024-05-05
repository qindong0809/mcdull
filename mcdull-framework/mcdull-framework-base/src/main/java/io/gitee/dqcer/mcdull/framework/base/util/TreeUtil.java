package io.gitee.dqcer.mcdull.framework.base.util;

import io.gitee.dqcer.mcdull.framework.base.vo.TreeVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * tree 工具类
 *
 * @author dqcer
 * @since 2022/12/09
 */
public class TreeUtil {

    private TreeUtil(){
        throw new AssertionError();
    }

    /**
     * 得到子树对象
     *
     * @param list     列表
     * @param parentId 父id
     * @return {@link List}<{@link T}>
     */
    public static <T extends TreeVO<T, ID>, ID extends Serializable> List<T> getChildTreeObjects(List<T> list, ID parentId) {
        List<T> returnList = new ArrayList<>(Collections.emptyList());
        for (T res : list) {
            if (res.getParentId() == null) {
                continue;
            }
            if (Objects.equals(res.getParentId(), parentId)) {
                recursionFn(list, res);
                returnList.add(res);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list the list
     * @param t    the t
     */
    public static <T extends TreeVO<T, ID>, ID extends Serializable> void recursionFn(List<T> list, T t) {
        List<T> children = getChildList(list, t);
        t.setChildren(children);
        t.setHasChild(true);
        for (T nextChild : children) {
            boolean hasChild = false;
            // 下一个对象，与所有的资源集进行判断
            if (hasChild(list, nextChild)) {
                // 有下一个子节点,递归
                for (T node : children) {
                    // 所有的对象--跟当前这个childList 的对象子节点
                    recursionFn(list, node);
                }
                hasChild = true;
            }
            nextChild.setHasChild(hasChild);
        }
    }

    /**
     * 获得指定节点下的所有子节点
     *
     * @param list the list
     * @param t    the t
     *
     * @return the child list
     */
    public static <T extends TreeVO<T, ID>, ID extends Serializable> List<T> getChildList(List<T> list, T t) {
        List<T> childList = new ArrayList<>(Collections.emptyList());
        for (T child : list) {
            if (Objects.isNull(child.getParentId())) {
                continue;
            }
            // 判断集合的父ID是否等于上一级的id
            if (Objects.equals(child.getParentId(), t.getId())) {
                childList.add(child);
            }
        }
        return childList;
    }

    /**
     * 判断是否还有下一个子节点
     *
     * @param list the list
     * @param t    the t
     *
     * @return the boolean
     */
    public static <T extends TreeVO<T, ID>, ID extends Serializable> boolean hasChild(List<T> list, T t) {
        return !getChildList(list, t).isEmpty();
    }
}
