package com.dqcer.mcdull.mdc.provider.config.excel;

/**
 * 错误备注枚举
 *
 * @author dqcer
 * @date 2022/11/20 22:11:74
 */
public enum ErrorRemarkEnum {

    required("1", "必填"),

    uq("2", "唯一性"),

    length_lawless("3", "长度非法"),

    need_approval("7", "需要入组并审批"),

    less_than("8", "小于"),

    not_find("9", "没有找到"),

    more_than("10", "大于");

    private String code;
    private String name;


    ErrorRemarkEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
