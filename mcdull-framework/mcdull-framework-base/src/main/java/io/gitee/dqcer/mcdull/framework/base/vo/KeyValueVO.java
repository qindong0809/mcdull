package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.supert.KeyValue;

/**
 * key value 视图对象
 *
 * @author dqcer
 * @date 2022/12/07
 */
public class KeyValueVO<K, V> implements KeyValue {

    private static final long serialVersionUID = 1L;

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

    public KeyValueVO<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public V getValue() {
        return value;
    }

    public KeyValueVO<K, V> setValue(V value) {
        this.value = value;
        return this;
    }
}
