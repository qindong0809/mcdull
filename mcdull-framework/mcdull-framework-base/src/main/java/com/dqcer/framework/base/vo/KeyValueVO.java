package com.dqcer.framework.base.vo;

/**
 * key value 视图对象
 *
 * @author dqcer
 * @date 2022/12/07
 */
public abstract class KeyValueVO<K, V> implements VO{

    private static final long serialVersionUID = 8433439505613070766L;

    /**
     * key
     */
    protected K id;

    /**
     * value
     */
    protected V name;

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public V getName() {
        return name;
    }

    public void setName(V name) {
        this.name = name;
    }
}
