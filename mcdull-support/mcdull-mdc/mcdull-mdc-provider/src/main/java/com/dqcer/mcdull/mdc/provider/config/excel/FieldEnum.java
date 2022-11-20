package com.dqcer.mcdull.mdc.provider.config.excel;


/**
 * 字段枚举
 *
 * @author dqccer
 * @date 2022/11/20 22:11:84
 */
public enum FieldEnum {


    finishTime("finishTime", "完成时间");

    private String key;
    
    private String value;
    
    FieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
