package io.gitee.dqcer.mcdull.system.facade.model.enums;


import lombok.Getter;

import java.util.Arrays;

/**
 * 性别枚举（共享给业务模块）
 * <p>
 * 契约包只描述数据形状，不实现 starter-web 的 IEnum：IEnum 的 code/text 存在静态字典池里、
 * getText 还要经容器取 i18n，那是展示层能力。facade 只保留自身字段，字典能力留给实现方。
 */
@Getter
public enum GenderEnum {

    /**
     * 0 未知
     */
    UNKNOWN(0, "未知"),

    /**
     * 男 1 奇数为阳
     */
    MAN(1, "男"),

    /**
     * 女 2 偶数为阴
     */
    WOMAN(2, "女");


    private final Integer code;

    private final String text;

    GenderEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 按 code 取展示文本，取不到返回 null
     *
     * @param code 编码
     * @return {@link String}
     */
    public static String textOf(Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .map(GenderEnum::getText)
                .findAny().orElse(null);
    }
}
