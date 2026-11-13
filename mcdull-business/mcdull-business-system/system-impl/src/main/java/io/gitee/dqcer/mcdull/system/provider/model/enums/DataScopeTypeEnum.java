package io.gitee.dqcer.mcdull.system.provider.model.enums;


import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;
import lombok.Getter;

/**
 * 登录设备类型
 *
 * @author dqcer
 * @since 2024/04/24
 */
@Getter
public enum DataScopeTypeEnum implements IEnum<Integer> {

    NOTICE(1, 20, "系统通知", "系统通知数据范围"),
    ;

    private final Integer sort;

    private final String name;

    DataScopeTypeEnum(Integer code, Integer sort, String name, String text) {
        init(code, text);
        this.sort = sort;
        this.name = name;
    }



}
