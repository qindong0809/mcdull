package com.dqcer.framework.base.mq;


/**
 * MQ消息接口
 * T 消息体类型
 *
 * @author dqcer
 * @date 2022/10/05
 */
@SuppressWarnings("unused")
public interface MqTopic<T> {

    /**
     * 发送消息
     *
     * @param message 消息体
     */
    default void product(T message) {
    }

    /**
     * 自定义发送消息
     *
     * @param mqConfig 消息配置
     * @param message  消息体
     */
    default void product(MqConfig mqConfig, T message) {
    }

    /**
     * 消费消息
     *
     * @param message 消息体
     */
    void consumer(T message);
}
