package io.gitee.dqcer.mcdull.framework.scheduler.interceptor;

import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.Execution;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.VoidExecutionHandler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.CronSchedule;
import com.github.kagkarlsson.scheduler.task.schedule.FixedDelay;
import com.github.kagkarlsson.scheduler.task.schedule.Schedule;
import io.gitee.dqcer.mcdull.framework.scheduler.annotation.McdullScheduledHolder;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduledTask;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduleDefinition;

import java.lang.reflect.Method;
import java.time.Instant;

/**
 * 将 McDull 的任务定义（注解 / 接口）转换为 db-scheduler 的原生 Task 实现。
 * <p>这是一个纯工具类，负责两层转换：
 * <pre>
 *   @McdullScheduled 注解  →  Tasks.recurring().execute()  →  RecurringTask
 *   McdullScheduledTask<T>  →  Tasks.recurring().execute()  →  RecurringTask<T>
 * </pre>
 *
 * @author dqcer
 * @since 1.0.0
 */
public final class TaskConverter {

    private TaskConverter() {
        // 工具类
    }

    /**
     * 将 McdullScheduleDefinition 转换为 db-scheduler 的 Schedule。
     */
    public static Schedule toSchedule(McdullScheduleDefinition definition) {
        if (definition.getCron() != null && !definition.getCron().isEmpty()) {
            return new CronSchedule(normalizeCron(definition.getCron()));
        }
        if (definition.getFixedDelayMs() != null && definition.getFixedDelayMs() > 0) {
            return FixedDelay.ofMillis(definition.getFixedDelayMs());
        }
        throw new IllegalArgumentException(
                "Schedule 必须指定 cron 或 fixedDelayMs，任务: " + definition.getName());
    }

    /**
     * 归一化 cron 表达式。
     * <p>db-scheduler 基于 cron-utils 的 Quartz 定义，要求 6 或 7 段（秒 分 时 日 月 周 [年]）；
     * 而 Linux crontab 以及不少存量配置是 5 段（分 时 日 月 周）。
     * 这里对 5 段表达式自动补上秒位（{@code 0}），保证两种写法都可用。
     */
    private static String normalizeCron(String cron) {
        String trimmed = cron.trim();
        if (trimmed.split("\\s+").length == 5) {
            return "0 " + trimmed;
        }
        return trimmed;
    }

    /**
     * 将 McdullScheduledTask 接口实现转换为 db-scheduler RecurringTask。
     */
    @SuppressWarnings("unchecked")
    public static <T> RecurringTask<T> convertInterfaceTask(McdullScheduledTask<T> mcdullTask,
                                                            McdullScheduleDefinition definition,
                                                            McdullExecutionInterceptor interceptor) {
        Schedule schedule = toSchedule(definition);

        if (mcdullTask.isStateful()) {
            return Tasks.<T>recurring(definition.getName(), schedule, mcdullTask.dataType())
                    .execute((TaskInstance<T> instance, ExecutionContext ctx) ->
                            doExecute(mcdullTask, definition, interceptor, instance, ctx));
        }

        // 无状态任务不传数据类型，避免 db-scheduler 尝试反序列化 Void
        return (RecurringTask<T>) Tasks.recurring(definition.getName(), schedule)
                .execute((TaskInstance<Void> instance, ExecutionContext ctx) ->
                        doExecute(mcdullTask, definition, interceptor, (TaskInstance<T>) instance, ctx));
    }

    private static <T> void doExecute(McdullScheduledTask<T> mcdullTask,
                                      McdullScheduleDefinition definition,
                                      McdullExecutionInterceptor interceptor,
                                      TaskInstance<T> instance,
                                      ExecutionContext ctx) {
        String taskName = definition.getName();
        String instanceId = instance.getId();
        long start = System.currentTimeMillis();
        Exception error = null;
        boolean success = false;

        Long recordId = interceptor.beforeExecution(taskName, instanceId,
                scheduledAt(ctx), attemptNumber(ctx));
        try {
            mcdullTask.execute(instance, ctx);
            mcdullTask.onSuccess(instance, ctx);
            success = true;
        } catch (Exception e) {
            error = e;
            mcdullTask.onFailure(e, instance, ctx);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;
            // 用 success 标记而非 error == null：Error（OOM 等）不会进入 catch，此时也必须记为失败
            interceptor.afterExecution(recordId, taskName, instanceId, duration, success, error);
        }
    }

    /**
     * 将 @McdullScheduled 注解方法转换为 db-scheduler RecurringTask。
     */
    public static RecurringTask<Void> convertAnnotationHolder(McdullScheduledHolder holder,
                                                              McdullExecutionInterceptor interceptor) {
        McdullScheduleDefinition definition = McdullScheduleDefinition.builder()
                .name(holder.getAnnotation().name())
                .cron(holder.getAnnotation().cron())
                .fixedDelayMs(holder.getAnnotation().fixedDelayMs() > 0
                        ? holder.getAnnotation().fixedDelayMs() : 0L)
                .fixedRateMs(holder.getAnnotation().fixedRateMs() > 0
                        ? holder.getAnnotation().fixedRateMs() : 0L)
                .maxRetries(holder.getAnnotation().maxRetries())
                .backoff(holder.getAnnotation().backoff())
                .stateful(false)
                .enabled(holder.getAnnotation().enabled())
                .build();

        Schedule schedule = toSchedule(definition);
        Method method = holder.getMethod();
        Object bean = holder.getBean();

        return Tasks.recurring(definition.getName(), schedule)
                .execute((VoidExecutionHandler<Void>) (TaskInstance<Void> instance, ExecutionContext ctx) -> {
                    String taskName = definition.getName();
                    String instanceId = instance.getId();
                    long start = System.currentTimeMillis();
                    Exception error = null;
                    boolean success = false;

                    Long recordId = interceptor.beforeExecution(taskName, instanceId,
                            scheduledAt(ctx), attemptNumber(ctx));
                    try {
                        method.invoke(bean);
                        success = true;
                    } catch (Exception e) {
                        error = e instanceof java.lang.reflect.InvocationTargetException
                                ? (Exception) ((java.lang.reflect.InvocationTargetException) e).getTargetException()
                                : e;
                        if (error instanceof RuntimeException re) {
                            throw re;
                        }
                        throw new RuntimeException(error);
                    } finally {
                        long duration = System.currentTimeMillis() - start;
                        interceptor.afterExecution(recordId, taskName, instanceId, duration, success, error);
                    }
                });
    }

    /**
     * 从执行上下文取计划执行时间（调度延迟 = 实际开始时间 - 计划执行时间）。
     */
    private static Instant scheduledAt(ExecutionContext ctx) {
        Execution execution = ctx == null ? null : ctx.getExecution();
        return execution == null ? null : execution.getExecutionTime();
    }

    /**
     * 从执行上下文取尝试次数（db-scheduler 的连续失败次数 + 1）。
     */
    private static int attemptNumber(ExecutionContext ctx) {
        Execution execution = ctx == null ? null : ctx.getExecution();
        return execution == null ? 1 : execution.consecutiveFailures + 1;
    }
}
