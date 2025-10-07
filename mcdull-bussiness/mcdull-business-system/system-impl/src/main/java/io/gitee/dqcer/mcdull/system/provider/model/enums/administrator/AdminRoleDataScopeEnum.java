package io.gitee.dqcer.mcdull.system.provider.model.enums.administrator;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
public enum AdminRoleDataScopeEnum implements IEnum<Integer> {

    ALL(1, "全部数据权限"),
    DEPT_AND_SUB(2, "本部门及以下数据权限"),
    DEPT(3, "本部门数据权限"),
    SELF(4, "仅本人数据权限"),
    CUSTOM(5, "自定义数据权限"),

    ;


    AdminRoleDataScopeEnum(Integer code, String text) {
        init(code, text);
    }

}
