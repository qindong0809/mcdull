package io.gitee.dqcer.mcdull.framework.base.vo;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.support.VO;

/**
 * 只有code和text，可以用于展示下拉框
 *
 * @author dqcer
 * @since 2022/10/04
 */
@SuppressWarnings("unused")
public class EnumVO<T> implements IEnum<T>, VO {

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
