package io.gitee.dqcer.mcdull.business.common.audit;

import io.gitee.dqcer.mcdull.framework.base.constants.SymbolConstants;

/**
 * 审计
 * @author dqcer
 * @since 2023/06/25
 */
public interface Audit {


    /**
     * 前缀, 考虑：嵌套场景
     *
     * @return {@link String}
     */
    default String prefix() {
        return "";
    }

    /**
     * 标记字符
     *
     * @return {@link String}
     */
    default String[] tagCharacter() {
        return new String[]{"", ""};
    }

    /**
     * eg:
     * separate :
     * key: value
     *
     * separate ->
     * key-> value
     *
     * @return {@link String}
     */
    default String separate() {
        return SymbolConstants.MH;
    }
}
