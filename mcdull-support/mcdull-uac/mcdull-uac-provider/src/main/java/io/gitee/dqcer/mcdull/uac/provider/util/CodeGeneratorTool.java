package io.gitee.dqcer.mcdull.uac.provider.util;

import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.StrUtil;

/**
 * 代码生成 velocity 工具类
 *
 */

public class CodeGeneratorTool {

    /**
     * 小写驼峰，转为大写驼峰
     */
    public String lowerCamel2UpperCamel(String str) {
        if (str == null) {
            return StrUtil.EMPTY;
        }
//        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, str);
        return StrUtil.lowerFirst(str);
    }

    /**
     * 小写驼峰，转为小写中划线
     */
    public String lowerCamel2LowerHyphen(String str) {
        if (str == null) {
            return StrUtil.EMPTY;
        }
//        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, str);
        // 使用正则表达式将大写字母前插入连字符，并转换为小写
        String hyphenStr = str.replaceAll("([A-Z])", "-$1").toLowerCase();
        // 去掉可能出现在字符串开头的连字符
        return hyphenStr.startsWith("-") ? hyphenStr.substring(1) : hyphenStr;
    }


    /**
     * 去掉 注释 枚举类型
     */
    public String removeEnumDesc(String str) {
        if (str == null) {
            return StrUtil.EMPTY;
        }

        int index = str.indexOf("[");
        if (index == -1) {
            index = str.indexOf("【");
        }

        if (index == -1) {
            return str;
        }

        return str.substring(0, index - 1);
    }

}
