package io.gitee.dqcer.framework.base.util;

/**
 * 字符串验证工具
 *
 * @author dqcer
 * @version 2022/10/04
 */
@SuppressWarnings("unused")
public class StrUtil {

    private StrUtil() {
        throw new AssertionError();
    }

    /**
     * 判断是否为空
     *
     * @param str str
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return null == str || str.length() == 0 || str.trim().length() == 0;
    }


    /**
     * 判断是否不为空
     *
     * @param str str
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 将字符串的首字母转大写
     *
     * @param str 需要转换的字符串
     * @return {@link String}
     */
    public static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs= str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
