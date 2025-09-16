package io.gitee.dqcer.mcdull.mdc.client.constants;

import io.gitee.dqcer.mcdull.framework.web.enums.IEnum;

/**
 * @author dqcer
 */

public enum DictEnum implements IEnum<String> {


    STATUS_TYPE("status_type", "状态标识"),

    ;

    DictEnum(String code, String text) {
        init(code, text);
    }


}
