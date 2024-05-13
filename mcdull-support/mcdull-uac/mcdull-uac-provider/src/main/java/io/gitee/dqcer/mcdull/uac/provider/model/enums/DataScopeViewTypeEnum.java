package io.gitee.dqcer.mcdull.uac.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import lombok.Getter;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
@Getter
public enum DataScopeViewTypeEnum implements IEnum<Integer> {

    /**
     * 本人
     */
    ME(0, 0, "本人"),

    /**
     * 部门
     */
    DEPARTMENT(1, 5, "本部门"),

    /**
     * 本部门及下属子部门
     */
    DEPARTMENT_AND_SUB(2, 10, "本部门及下属子部门"),

    /**
     * 全部
     */
    ALL(10, 100, "全部");
    ;

    private final Integer level;


    DataScopeViewTypeEnum(Integer code, Integer level,  String text) {
        init(code, text);
        this.level = level;
    }



}
