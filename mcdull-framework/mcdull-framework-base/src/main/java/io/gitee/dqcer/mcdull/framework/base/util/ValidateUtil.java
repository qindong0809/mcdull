package io.gitee.dqcer.mcdull.framework.base.util;

import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author dqcer
 * @date 2022/12/19
 */
public class ValidateUtil {

    private ValidateUtil(){
        throw new AssertionError();
    }

    /**
     * regexp用户名
     */
    public static final String REGEXP_USERNAME = "^[a-z0-9_-]{3,16}$";

    /**
     * regexp密码
     */
    public static final String REGEXP_PASSWORD = "^[a-z0-9_-]{6,18}$";

    /**
     * regexp邮件
     */
    public static final String REGEXP_EMAIL = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
    /**
     * regexp电话
     */
    public static final String REGEXP_PHONE = "^(13[0-9]|14[0-9]|15[0-9]|170|177|18[0-9])\\d{8}$";

    /**
     * regexp url
     */
    public static final String REGEXP_URL = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";
    /**
     * regexp ipv4
     */
    public static final String REGEXP_IPV4 = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    /**
     * regexp ipv6
     */
    public static final String REGEXP_IPV6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    /**
     * regexp html
     */
    public static final String REGEXP_HTML = "^<([a-z]+)([^<]+)*(?:>(.*)<\\/\\1>|\\s+\\/>)$";
    /**
     * Unicode编码中的汉字范围
     */
    public static final String REGEXP_CHINESE = "^[\u2E80-\u9FFF]+$";

    /**
     * 是有效电子邮件
     *
     * @param email 电子邮件
     * @return boolean
     */
    public static boolean isValidEmail(String email) {
        return matchRegExp(email, REGEXP_EMAIL);
    }

    /**
     * 有效电话
     *
     * @param phone 电话
     * @return boolean
     */
    public static boolean isValidPhone(String phone) {
        return matchRegExp(phone, REGEXP_PHONE);
    }

    /**
     * 检查字符串是否匹配某个正则表达式
     *
     * @param str   字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean matchRegExp(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(str).matches();
    }
}
