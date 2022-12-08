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
    protected K key;

    /**
     * value
     */
    protected V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
