package io.gitee.dqcer.mcdull.framework.base.supert;

import java.io.Serializable;

/**
 * key value
 *
 * @author dqcer
 * @since 2023/04/13
 */
public interface KeyValue<K, V> extends Serializable {

    /**
     * key
     *
     * @return {@link K}
     */
    K getKey();

    /**
     * value
     *
     * @return {@link V}
     */
    V getValue();
}
