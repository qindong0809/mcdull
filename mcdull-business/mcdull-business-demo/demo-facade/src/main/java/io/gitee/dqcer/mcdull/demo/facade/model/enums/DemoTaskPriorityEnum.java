package io.gitee.dqcer.mcdull.demo.facade.model.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 演示任务优先级契约枚举。
 */
@Getter
public enum DemoTaskPriorityEnum {

    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高");

    private final Integer code;
    private final String text;

    DemoTaskPriorityEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 供 @EnumsIntValid 反射调用。
     *
     * @param clazz 枚举类型，仅用于保持统一的反射签名
     * @param code  优先级编码
     * @return 匹配的枚举，未匹配返回 null
     */
    public static DemoTaskPriorityEnum getByCode(Class<? extends DemoTaskPriorityEnum> clazz, Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElse(null);
    }

    /**
     * 按 code 取展示文本。
     *
     * @param code 优先级编码
     * @return 文本，未匹配返回 null
     */
    public static String textOf(Integer code) {
        DemoTaskPriorityEnum item = getByCode(DemoTaskPriorityEnum.class, code);
        return item == null ? null : item.getText();
    }
}
