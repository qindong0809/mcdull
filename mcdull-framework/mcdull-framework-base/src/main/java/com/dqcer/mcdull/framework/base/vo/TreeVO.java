package com.dqcer.mcdull.framework.base.vo;

import java.util.List;

/**
 * 树型数据对象
 *
 * @author dqcer
 * @version 2022/12/07
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
    protected ID pid;

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
        final StringBuffer sb = new StringBuffer("TreeVO{");
        sb.append("id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", hasChild=").append(hasChild);
        sb.append(", children=").append(children);
        sb.append('}');
        return sb.toString();
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ID getPid() {
        return pid;
    }

    public void setPid(ID pid) {
        this.pid = pid;
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
