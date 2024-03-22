package io.gitee.dqcer.mcdull.framework.base.vo;

/**
 * @author dqcer
 */
public class LabelValueVO<V, L> {

    /**
     * key
     */
    protected V value;

    /**
     * name
     */
    protected L label;

    public LabelValueVO(V value, L label) {
        this.value = value;
        this.label = label;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public L getLabel() {
        return label;
    }

    public void setLabel(L label) {
        this.label = label;
    }
}
