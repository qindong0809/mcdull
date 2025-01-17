package io.gitee.dqcer.mcdull.framework.web.jmx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * ThreadPool JMX适配器
 *
 * @author dqcer
 * @since 2024/03/27
 */
@Component
@ManagedResource(
        objectName = "Customization:name=io.gitee.dqcer.mcdull.framework.web.jmx.ThreadPoolJmxAdapter",
        description = "redis util")
public class ThreadPoolJmxAdapter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    // set get
    @ManagedAttribute()
    public int getCorePoolSize(){
        return threadPoolTaskExecutor.getCorePoolSize();
    }

    @ManagedAttribute()
    public void setCorePoolSize(int size){
        threadPoolTaskExecutor.setCorePoolSize(size);
    }

    @ManagedAttribute()
    public int getMaxPoolSize(){
        return threadPoolTaskExecutor.getMaxPoolSize();
    }

    @ManagedAttribute()
    public void setMaxPoolSize(int size){
        threadPoolTaskExecutor.setMaxPoolSize(size);
    }

    @ManagedAttribute()
    public int getKeepAliveSeconds(){
        return threadPoolTaskExecutor.getKeepAliveSeconds();
    }

    @ManagedAttribute()
    public void setKeepAliveSeconds(int size){
        threadPoolTaskExecutor.setKeepAliveSeconds(size);
    }


    //only get
    @ManagedAttribute()
    public int getPoolSize(){
        return threadPoolTaskExecutor.getThreadPoolExecutor().getPoolSize();
    }

    @ManagedAttribute()
    public int getActiveCount(){
        return threadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount();
    }

    @ManagedAttribute()
    public int getQueueSize(){
        return threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size();
    }

    @ManagedAttribute()
    public int getRemainingCapacity(){
        return threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity();
    }

    @ManagedAttribute()
    public long getTaskCount(){
        return threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount();
    }
}
