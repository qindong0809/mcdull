package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 删除类型
 *
 */
public enum CodeDeleteEnum implements IEnum<String> {

    /**
     * 单个删除
     */
    SINGLE("Single", "单个删除"),

    /**
     * 批量删除
     */
    BATCH("Batch", "批量删除"),

    /**
     * 单个和批量删除
     */
    SINGLE_AND_BATCH("SingleAndBatch", "单个和批量删除");


    CodeDeleteEnum(String value, String desc) {
        init(value, desc);
    }
}
