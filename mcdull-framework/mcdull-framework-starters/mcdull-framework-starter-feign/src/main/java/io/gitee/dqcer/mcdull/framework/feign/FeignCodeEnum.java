package io.gitee.dqcer.mcdull.framework.feign;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ICode;

public enum FeignCodeEnum implements ICode, IEnum<Integer> {

    /**
     * feign 调用异常
     */
    FEIGN_BIZ(999520, "上游服务调用异常： {0}"),
    ;

    /**
     * 状态码
     */
    final int code;

    /**
     * 消息
     */
    final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    /**
     * message
     *
     * @return {@link String}
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    FeignCodeEnum(int code, String message) {
        init(code, message);
        this.code = code;
        this.message = message;
    }
}
