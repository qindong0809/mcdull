package io.gitee.dqcer.mcdull.framework.base.util;

import java.util.Collection;
import java.util.Map;

/**
 * obj工具
 *
 * @author dqcer
 * @since 2022/10/04
 */
public class ObjUtil {

    /**
     * 禁止实例化
     */
    private ObjUtil() {
        throw new AssertionError();
    }

    /**
     * 判断是否为空
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNull(Object obj) {
        if (null == obj) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return StrUtil.isBlank(String.valueOf(obj));
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        return false;

    }

    /**
     * 判断是否不为空
     *
     * @param obj obj
     * @return boolean
     */
    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    /**
     * 如果空返回默认值
     *
     * @param object       对象
     * @param defaultValue 默认的值
     * @return {@link T}
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return (null != object) ? object : defaultValue;
    }
}
