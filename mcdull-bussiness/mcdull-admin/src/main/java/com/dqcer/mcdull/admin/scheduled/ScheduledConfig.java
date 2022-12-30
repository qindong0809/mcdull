package com.dqcer.mcdull.admin.scheduled;

import com.dqcer.mcdull.framework.web.transform.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;


/**
 * 定时任务配置
 *
 * @author dqcer
 * @version 2022/12/29
 */
@EnableScheduling
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        try {
            String className ="com.dqcer.mcdull.admin.scheduled.job.TestJob";
            String cron = "0/5 * * * * ?";
            taskRegistrar(taskRegistrar, className, cron);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void taskRegistrar(ScheduledTaskRegistrar taskRegistrar, String className, String cron) throws Exception {
        Class<?> clazz = Class.forName(className);
        Object task = SpringContextHolder.getBean(clazz);
        CronTrigger cronTrigger = new CronTrigger(cron);
        cronTrigger.nextExecutionTime(new SimpleTriggerContext());
        taskRegistrar.addTriggerTask(((Runnable) task), cronTrigger);
    }
}