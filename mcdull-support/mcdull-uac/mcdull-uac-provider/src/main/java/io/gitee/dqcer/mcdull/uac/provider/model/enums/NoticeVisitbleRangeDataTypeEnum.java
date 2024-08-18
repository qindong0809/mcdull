package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum NoticeVisitbleRangeDataTypeEnum implements IEnum<Integer> {

    /**
     * 员工
     */
    EMPLOYEE(1, "员工"),

    /**
     * 部门
     */
    DEPARTMENT(2, "部门"),
    ;


    NoticeVisitbleRangeDataTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
