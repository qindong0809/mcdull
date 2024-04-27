package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * user type
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum UserTypeEnum implements IEnum<Integer> {

    /**
     * 管理端 员工用户
     */
    ADMIN_EMPLOYEE(1, "员工");
    ;


    UserTypeEnum(Integer code, String text) {
        init(code, text);
    }

    public static UserTypeEnum toEnum(Integer code) {
        switch (code) {
            case 1:
                return UserTypeEnum.ADMIN_EMPLOYEE;
            default:
                throw new IllegalArgumentException("invalid value , only [1] is allowed");
        }
    }

}
