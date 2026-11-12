package io.gitee.dqcer.mcdull.framework.scheduler.registry;

import io.gitee.dqcer.mcdull.framework.scheduler.annotation.McdullScheduledHolder;
import io.gitee.dqcer.mcdull.framework.scheduler.node.McdullScheduledTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务注册表。
 * <p>统一存储两类任务：
 * <ul>
 *     <li>接口实现：{@link McdullScheduledTask} Bean</li>
 *     <li>注解方法：{@link McdullScheduledHolder}（被 {@code @McdullScheduled} 标记的方法）</li>
 * </ul>
 *
 * @author dqcer
 * @since 1.0.0
 */
@Slf4j
@Getter
public class TaskRegistry {

    /** 接口实现的任务 Bean */
    private final List<McdullScheduledTask<?>> interfaceTasks = new ArrayList<>();

    /** 注解标记的方法 */
    private final List<McdullScheduledHolder> annotationHolders = new ArrayList<>();

    public void register(McdullScheduledTask<?> task) {
        log.info("[mcdull-scheduler] 注册接口任务: {}", task.definition().getName());
        interfaceTasks.add(task);
    }

    public void register(McdullScheduledHolder holder) {
        log.info("[mcdull-scheduler] 注册注解任务: {}", holder.getName());
        annotationHolders.add(holder);
    }

    /**
     * 合并所有已注册任务的名称。
     */
    public List<String> allTaskNames() {
        List<String> names = new ArrayList<>();
        interfaceTasks.forEach(t -> names.add(t.definition().getName()));
        annotationHolders.forEach(h -> names.add(h.getName()));
        return names;
    }

    public boolean isEmpty() {
        return interfaceTasks.isEmpty() && annotationHolders.isEmpty();
    }
}
