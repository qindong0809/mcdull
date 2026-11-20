package io.gitee.dqcer.mcdull.demo.facade.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 演示任务状态。
 * <p>
 * 契约包中的枚举是纯 POJO：不实现 starter-web 的 IEnum，不依赖字典池或 i18n 容器。
 * 静态 getByCode(Class, code) 用于适配框架 {@code @EnumsIntValid} 校验器；
 * 展示层文本由调用方通过 {@link #textOf(Integer)} 自行翻译。
 */
@Getter
public enum DemoTaskStatusEnum {

    TODO(0, "待办"),
    DOING(1, "进行中"),
    DONE(2, "已完成"),
    CANCELLED(3, "已取消");

    private final Integer code;
    private final String text;

    DemoTaskStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 供 @EnumsIntValid 反射调用，第二个参数必须是 code。
     *
     * @param clazz 枚举类型，仅用于保持统一的反射签名
     * @param code  状态码
     * @return 匹配的枚举，未匹配返回 null
     */
    public static DemoTaskStatusEnum getByCode(Class<? extends DemoTaskStatusEnum> clazz, Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElse(null);
    }

    /**
     * 按 code 取展示文本。
     *
     * @param code 状态码
     * @return 文本，未匹配返回 null
     */
    public static String textOf(Integer code) {
        DemoTaskStatusEnum item = getByCode(DemoTaskStatusEnum.class, code);
        return item == null ? null : item.getText();
    }
}
