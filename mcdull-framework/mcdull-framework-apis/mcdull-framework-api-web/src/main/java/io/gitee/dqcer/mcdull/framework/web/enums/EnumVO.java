package io.gitee.dqcer.mcdull.framework.web.enums;

import java.io.Serializable;

/**
 * 只有code和text，可以用于展示下拉框。
 * <p>
 * 文本国际化由 starter-web 在启动时注入 {@link TextResolver}；
 * 未接入 starter 时返回原始文本，避免 API 模块反向依赖 Spring 容器。
 *
 * @author dqcer
 * @since 2022/10/04
 */
@SuppressWarnings("unused")
public class EnumVO<T> implements IEnum<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static volatile TextResolver textResolver = text -> text;

    private T code;

    private String text;

    private EnumVO() {
    }

    public EnumVO(T code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 由 starter-web 注入国际化解析器。
     *
     * @param resolver 解析器，null 时恢复默认原始文本
     */
    public static void setTextResolver(TextResolver resolver) {
        textResolver = resolver == null ? text -> text : resolver;
    }

    @Override
    public T getCode() {
        return code;
    }

    @Override
    public String getText() {
        if (text == null || text.isBlank()) {
            return text;
        }
        String resolved = textResolver.resolve(text);
        return resolved == null || resolved.isBlank() ? text : resolved;
    }

    @Override
    public String toString() {
        return "EnumVO{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }

    /**
     * 枚举文本解析策略。
     */
    @FunctionalInterface
    public interface TextResolver {

        /**
         * 解析展示文本。
         *
         * @param text 原始文本
         * @return 国际化后的文本
         */
        String resolve(String text);
    }
}
