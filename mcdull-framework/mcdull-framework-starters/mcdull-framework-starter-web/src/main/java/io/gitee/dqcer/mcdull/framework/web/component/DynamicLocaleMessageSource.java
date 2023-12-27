package io.gitee.dqcer.mcdull.framework.web.component;

/**
 * @author dqcer
 * @since 2023/12/27
 */
public interface DynamicLocaleMessageSource {
    String getMessage(String code, Object[] args);

    String getMessage(String code);
}
