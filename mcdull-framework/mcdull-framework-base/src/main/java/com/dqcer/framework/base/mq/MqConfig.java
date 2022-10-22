package com.dqcer.framework.base.mq;

import java.io.Serializable;

/**
 * MQ消息配置
 *
 * @author dqcer
 * @date 2022/10/05
 */
@SuppressWarnings("unused")
public class MqConfig implements Serializable {

    private static final long serialVersionUID = 4071640175148465559L;

    /**
     * tag
     */
    private String tag;

    /**
     * 设置了同一hashKey的消息可保证消费顺序
     */
    private String hashKey;

    /**
     * 延迟消息的延迟级别
     */
    private MqDelayLevel delayLevel;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public MqDelayLevel getDelayLevel() {
        return delayLevel;
    }

    public void setDelayLevel(MqDelayLevel delayLevel) {
        this.delayLevel = delayLevel;
    }
}
