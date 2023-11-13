package io.gitee.dqcer.mcdull.framework.base.bo;

import io.gitee.dqcer.mcdull.framework.base.supert.KeyValue;

import java.util.StringJoiner;

/**
 * key value bo
 *
 * @author dqcer
 * @since 2023/04/13
 */
public class KeyValueBO<K, V> implements KeyValue<K, V> {

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    protected K key;

    /**
     * value
     */
    protected V value;

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyValueBO.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value=" + value)
                .toString();
    }

    @Override
    public K getKey() {
        return key;
    }

    public KeyValueBO<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    @Override
    public V getValue() {
        return value;
    }

    public KeyValueBO<K, V> setValue(V value) {
        this.value = value;
        return this;
    }
}
