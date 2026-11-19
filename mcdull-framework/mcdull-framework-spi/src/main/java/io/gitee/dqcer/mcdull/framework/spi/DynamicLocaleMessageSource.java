package io.gitee.dqcer.mcdull.framework.spi;

/**
 * 国际化文案取值契约，由 starter-web 提供实现，鉴权等能力模块按接口注入。
 * @author dqcer
 * @since 2023/12/27
 */
public interface DynamicLocaleMessageSource {

    String getMessage(String languageStr, String code, Object[] args);

    String getMessage(String code, Object[] args);

    String getMessage(String code);
}
