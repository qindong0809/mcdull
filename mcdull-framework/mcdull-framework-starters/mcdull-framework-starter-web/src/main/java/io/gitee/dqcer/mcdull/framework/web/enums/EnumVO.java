package io.gitee.dqcer.mcdull.framework.web.enums;

import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.framework.web.component.DynamicLocaleMessageSource;
import io.gitee.dqcer.mcdull.framework.web.transform.SpringContextHolder;

import java.io.Serializable;

/**
 * 只有code和text，可以用于展示下拉框
 *
 * @author dqcer
 * @since 2022/10/04
 */
@SuppressWarnings("unused")
public class EnumVO<T> implements IEnum<T>, Serializable {

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
        DynamicLocaleMessageSource dynamicLocaleMessageSource = SpringContextHolder.getBean(DynamicLocaleMessageSource.class);
        if (CharSequenceUtil.isNotBlank(text)) {
            String i18nValue = dynamicLocaleMessageSource.getMessage(text);
            if (CharSequenceUtil.isNotBlank(i18nValue)) {
                return i18nValue;
            }
        }
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
