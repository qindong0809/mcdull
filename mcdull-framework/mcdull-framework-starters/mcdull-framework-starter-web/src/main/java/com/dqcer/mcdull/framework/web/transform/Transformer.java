package com.dqcer.mcdull.framework.web.transform;

/**
 * 变压器
 *
 * @author dqcer
 * @date 2022/10/05
 */
@FunctionalInterface
public interface Transformer<ID> {


    /**
     * 翻译
     *
     * @param original 原始值
     * @param datasource 翻译数据源
     * @param param 翻译后取值参数名字
     * @return 翻译后的值
     */
    String transform(ID original, Class<?> datasource, String param);
}