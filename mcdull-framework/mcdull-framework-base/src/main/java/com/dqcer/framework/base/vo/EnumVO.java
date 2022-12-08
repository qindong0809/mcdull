package com.dqcer.framework.base.vo;

import com.dqcer.framework.base.enums.IEnum;

/**
 * 只有code和text，可以用于展示下拉框
 *
 * @author dqcer
 * @version 2022/10/04
 */
@SuppressWarnings("unused")
public class EnumVO<T> implements IEnum<T> {

    private static final long serialVersionUID = 1L;

    private T code;

    private String text;

    private EnumVO() {
    }


    public EnumVO(T code, String text) {
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
        return "EnumVO{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }
}
