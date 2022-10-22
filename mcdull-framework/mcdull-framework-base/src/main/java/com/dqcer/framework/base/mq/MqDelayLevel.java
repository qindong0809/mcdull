package com.dqcer.framework.base.mq;


import com.dqcer.framework.base.dict.IDict;

/**
 * mq 延时时间等级
 *
 * @author dqcer
 * @date 2022/10/05
 */
@SuppressWarnings("unused")
public enum MqDelayLevel implements IDict<Integer> {

    /**
     * 时间阈值
     */
    TIME_1S(1, "1s"),
    TIME_5S(2, "5s"),
    TIME_10S(3, "10s"),
    TIME_30S(4, "30s"),
    TIME_1M(5, "1m"),
    TIME_2M(6, "2m"),
    TIME_3M(7, "3m"),
    TIME_4M(8, "4m"),
    TIME_5M(9, "5m"),
    TIME_6M(10, "6m"),
    TIME_7M(11, "7m"),
    TIME_8M(12, "8m"),
    TIME_9M(13, "9m"),
    TIME_10M(14, "10m"),
    TIME_20M(15, "20m"),
    TIME_30M(16, "30m"),
    TIME_1H(17, "1h"),
    TIME_2H(18, "2h"),
    ;

    MqDelayLevel(Integer code, String text) {
        init(code, text);
    }

}
