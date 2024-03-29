package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;

public class SelectOptionVO<T> implements VO {

    private String label;

    private T value;

    @Override
    public String toString() {
        return "SelectOptionVO{" +
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }

    public SelectOptionVO(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
