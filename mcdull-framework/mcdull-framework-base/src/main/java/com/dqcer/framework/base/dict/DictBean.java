package com.dqcer.framework.base.dict;

/**
 * 字典bean
 * 只有code和text，可以用于展示下拉框
 *
 * @author dqcer
 * @date 2022/10/04
 */
@SuppressWarnings("unused")
public class DictBean<T> implements IDict<T>{

    private static final long serialVersionUID = 1L;

    private T code;

    private String text;

    private DictBean() {
    }


    public DictBean(T code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public T getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "DictBean{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }
}
