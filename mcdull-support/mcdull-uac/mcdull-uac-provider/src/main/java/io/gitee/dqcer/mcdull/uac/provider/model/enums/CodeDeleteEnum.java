package io.gitee.dqcer.mcdull.uac.provider.model.enums;

import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;

/**
 * 删除类型
 *
 */
public enum CodeDeleteEnum implements IEnum<String> {

    SINGLE("Single", "单个删除"),
    BATCH("Batch", "批量删除"),
    SINGLE_AND_BATCH("SingleAndBatch", "单个和批量删除");


    CodeDeleteEnum(String value, String desc) {
        init(value, desc);
    }

    public static CodeDeleteEnum toEnum(String code) {
        switch (code) {
            case "Single":
                return CodeDeleteEnum.SINGLE;
            case "Batch":
                return CodeDeleteEnum.BATCH;
            case "SingleAndBatch":
                return CodeDeleteEnum.SINGLE_AND_BATCH;
            default:
                throw new IllegalArgumentException("invalid value , only [Single, Batch, SingleAndBatch] is allowed");
        }
    }


}
