package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.KeyValue;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

import java.util.StringJoiner;

/**
 * key value 视图对象
 *
 * @author dqcer
 * @since 2022/12/07
 */
public class KeyValueVO<K, V> implements KeyValue<K, V> , VO {

    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    protected K key;

    /**
     * value
     */
    protected V value;

    public KeyValueVO(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KeyValueVO.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value=" + value)
                .toString();
    }

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
