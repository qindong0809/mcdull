package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

/**
 * @author dqcer
 */
public class NameValueVO<L, V> implements VO {

    /**
     * key
     */
    protected V value;

    /**
     * name
     */
    protected L name;

    public NameValueVO(L label, V value) {
        this.value = value;
        this.name = label;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public L getName() {
        return name;
    }

    public void setName(L name) {
        this.name = name;
    }
}
