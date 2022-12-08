package com.dqcer.framework.base.vo;

import java.util.List;

/**
 * 树型数据对象
 *
 * @author dqcer
 * @version 2022/12/07
 */
public abstract class TreeVO<E, ID> implements VO {

    private static final long serialVersionUID = 5907562174896953932L;

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
    protected boolean hasChild = false;

    /**
     * 子节点集合
     */
    protected List<E> children;

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
        return hasChild;
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
