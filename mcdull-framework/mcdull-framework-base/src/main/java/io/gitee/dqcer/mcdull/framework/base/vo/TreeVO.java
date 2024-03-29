package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.List;
import java.util.StringJoiner;

/**
 * 树型数据对象
 *
 * @author dqcer
 * @since 2022/12/07
 */
public class TreeVO<E, ID> implements VO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    protected ID id;

    /**
     * 父ID
     */
    protected ID parentId;

    /**
     * 是否含有子节点
     */
    protected Boolean hasChild;

    /**
     * 子节点集合
     */
    protected List<E> children;

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeVO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("parentId=" + parentId)
                .add("hasChild=" + hasChild)
                .add("children=" + children)
                .toString();
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ID getParentId() {
        return parentId;
    }

    public void setParentId(ID parentId) {
        this.parentId = parentId;
    }

    public boolean isHasChild() {
        return hasChild == null ? false : hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public List<E> getChildren() {
        return children;
    }

    public void setChildren(List<E> children) {
        this.children = children;
    }
}
