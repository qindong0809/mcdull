package io.gitee.dqcer.mcdull.uac.provider.config.constants;

/**
 * 缓存常量
 *
 * @author dqcer
 * @version 2022/12/24
 */
public final class CacheConstants {

    /**
     * 码表缓存
     */
    public static final String DICT_LIST = "mcdull:uac:dict:{0}:{1}";
    public static final String DICT_ONE = "mcdull:uac:dict:{0}:{1}:{2}";
    public static final Long DICT_EXPIRE = 60 * 60 * 24L;
}
