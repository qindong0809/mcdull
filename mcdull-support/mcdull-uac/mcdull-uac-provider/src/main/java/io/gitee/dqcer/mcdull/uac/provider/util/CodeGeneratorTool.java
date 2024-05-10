package io.gitee.dqcer.mcdull.uac.provider.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;

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
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, str);
    }

    /**
     * 小写驼峰，转为小写中划线
     */
    public String lowerCamel2LowerHyphen(String str) {
        if (str == null) {
            return StrUtil.EMPTY;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, str);
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
