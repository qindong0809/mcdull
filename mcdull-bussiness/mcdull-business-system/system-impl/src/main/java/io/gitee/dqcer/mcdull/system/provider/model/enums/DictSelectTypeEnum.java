package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import lombok.Getter;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
@Getter
public enum DictSelectTypeEnum implements IEnum<String> {

    /**
     * sys_user_sex
     */
    USER_SEX("sys_user_sex", "性别"),

    /**
     * sys_user_type
     */
    USER_TYPE("sys_user_type", "用户类型"),

    /**
     * sys_user_status
     */
    USER_STATUS("sys_user_status", "用户状态"),

    /**
     * sys_dept_status
     */
    DEPT_STATUS("sys_dept_status", "部门状态"),

    /**
     * sys_role_status
     */
    ROLE_STATUS("sys_role_status", "角色状态"),

    /**
     * sys_role_data_scope
     */
    ROLE_DATA_SCOPE("sys_role_data_scope", "角色数据权限"),

    /**
     * sys_notice_type
     */
    NOTICE_TYPE("sys_notice_type", "通知类型"),

    /**
     * sys_notice_status
     */
    NOTICE_STATUS("sys_notice_status", "通知状态"),

    /**
     * sys_oper_type
     */
    OPER_TYPE("sys_oper_type", "操作类型"),

    /**
     * sys_common_status
     */
    COMMON_STATUS("sys_common_status", "状态"),

    /**
     * sys_job_group
     */
    JOB_GROUP("sys_job_group", "任务组"),

    CUSTOMER_TYPE("CUSTOMER_TYPE", "客户类型"),
    IN_ACTIVE("IN_ACTIVE", "状态"),


    ;


    DictSelectTypeEnum(String selectType,  String text) {
        init(selectType, text);
    }



}
