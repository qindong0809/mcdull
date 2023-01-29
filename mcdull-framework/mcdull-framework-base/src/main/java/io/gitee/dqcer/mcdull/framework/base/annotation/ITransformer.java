package io.gitee.dqcer.mcdull.framework.base.annotation;

/**
 * 翻译接口
 *
 * @author dqcer
 * @since 2022/10/05
 */
@FunctionalInterface
public interface ITransformer<ID> {


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
