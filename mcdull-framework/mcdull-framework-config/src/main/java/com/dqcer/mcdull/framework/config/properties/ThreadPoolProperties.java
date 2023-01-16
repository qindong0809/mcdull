package io.gitee.dqcer.framework.config.properties;

import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author dqcer
 * @version 2022/12/18
 */
public class ThreadPoolProperties {

    /**
     * 线程池中的核心线程数量,默认为2
     */
    private int corePoolSize = 2;

    /**
     * 线程池中的最大线程数量 10
     */
    private int maxPoolSize = 10;

    /**
     * 线程池中允许线程的空闲时间,默认为 60s
     */
    private int keepAliveTime = ((int) TimeUnit.SECONDS.toSeconds(60));

    /**
     * 线程池中的队列最大数量
     */
    private int queueCapacity = 10000;

    @Override
    public String toString() {
        return "ThreadPoolProperties{" +
                "corePoolSize=" + corePoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", queueCapacity=" + queueCapacity +
                '}';
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ThreadPoolProperties setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public ThreadPoolProperties setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public ThreadPoolProperties setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public ThreadPoolProperties setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        return this;
    }
}
