package io.gitee.dqcer.mcdull.admin.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 *
 * @author dqcer
 * @since 2022/07/26
 */
@SuppressWarnings("unused")
public enum UserTypeEnum implements IEnum<Integer> {

    READ_WRITE(1, ""),

    READ_ONLY(2, ""),

    ;

    UserTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
