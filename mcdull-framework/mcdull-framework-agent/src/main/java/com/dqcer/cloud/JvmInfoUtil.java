package com.dqcer.cloud;

import com.dqcer.cloud.model.ServiceGcLog;
import com.dqcer.cloud.model.ServiceLog;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * jvm  info util
 *
 * @author dqcer
 * @version  2022/11/11
 */
@SuppressWarnings("unused")
public class JvmInfoUtil {
    private static final long MB = 1048576L;

    /**
     * 执行前的内存信息
     *
     * @return {@link ServiceLog}
     */
    public static ServiceLog beforeMemoryInfo() {
        ServiceLog log = new ServiceLog();
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        //  返回用于对象分配的堆的当前内存使用情况
        MemoryUsage headMemory = memory.getHeapMemoryUsage();
        log.setBeforeHeadMemoryInit(headMemory.getInit() / MB);
        log.setBeforeHeadMemoryUsed(headMemory.getUsed() / MB);
        log.setBeforeHeadMemoryCommitted(headMemory.getCommitted() / MB);
        log.setBeforeHeadMemoryMax(headMemory.getMax() / MB);
        log.setBeforeHeadRate(headMemory.getUsed() * 100 / headMemory.getCommitted());

        //  返回Java虚拟机使用的非堆内存的当前内存使用情况
        MemoryUsage nonHeadMemory = memory.getNonHeapMemoryUsage();
        log.setBeforeNonHeadMemoryInit(nonHeadMemory.getInit() / MB);
        log.setBeforeNonHeadMemoryUsed(nonHeadMemory.getUsed() / MB);
        log.setBeforeNonHeadMemoryCommitted(nonHeadMemory.getCommitted() / MB);
        log.setBeforeNonHeadMemoryMax(nonHeadMemory.getMax() / MB);
        log.setBeforeNonHeadRate(nonHeadMemory.getUsed() * 100 / nonHeadMemory.getCommitted());
        return log;
    }

    /**
     * 执行后的内存信息
     *
     * @param beforeLog 之前日志
     * @return {@link ServiceLog}
     */
    public static ServiceLog afterMemoryInfo(ServiceLog beforeLog) {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        //  返回用于对象分配的堆的当前内存使用情况
        MemoryUsage headMemory = memory.getHeapMemoryUsage();
        beforeLog.setCostHeadMemoryInit(headMemory.getInit() / MB - beforeLog.getBeforeHeadMemoryInit());
        beforeLog.setCostHeadMemoryUsed(headMemory.getUsed() / MB - beforeLog.getBeforeHeadMemoryUsed());
        beforeLog.setCostHeadMemoryCommitted(headMemory.getCommitted() / MB - beforeLog.getBeforeHeadMemoryCommitted());
        beforeLog.setCostHeadMemoryMax(headMemory.getMax() / MB - beforeLog.getBeforeHeadMemoryMax());
        beforeLog.setCostHeadRate(headMemory.getUsed() * 100 / headMemory.getCommitted() - beforeLog.getBeforeHeadRate());

        //  返回Java虚拟机使用的非堆内存的当前内存使用情况
        MemoryUsage nonHeadMemory = memory.getNonHeapMemoryUsage();
        beforeLog.setCostNonHeadMemoryInit(nonHeadMemory.getInit() / MB - beforeLog.getBeforeNonHeadMemoryInit());
        beforeLog.setCostNonHeadMemoryUsed(nonHeadMemory.getUsed() / MB - beforeLog.getBeforeNonHeadMemoryUsed());
        beforeLog.setCostNonHeadMemoryCommitted(nonHeadMemory.getCommitted() / MB - beforeLog.getBeforeNonHeadMemoryCommitted());
        beforeLog.setCostNonHeadMemoryMax(nonHeadMemory.getMax() / MB - beforeLog.getBeforeNonHeadMemoryMax());
        beforeLog.setCostNonHeadRate(nonHeadMemory.getUsed() * 100 / nonHeadMemory.getCommitted() - beforeLog.getBeforeNonHeadRate());
        return beforeLog;
    }


    /**
     * 获取gc信息
     *
     * @return {@link List}<{@link ServiceGcLog}>
     */
    public static List<ServiceGcLog> getGCInfo(Long serviceLogId, Integer type) {
        List<ServiceGcLog> details = new ArrayList<>();
        List<GarbageCollectorMXBean> garbages = ManagementFactory.getGarbageCollectorMXBeans();
        ServiceGcLog detail;
        for (GarbageCollectorMXBean garbage : garbages) {
            detail = new ServiceGcLog();
            detail.setId(null);
            detail.setServiceLogId(serviceLogId);
            detail.setType(type);
            detail.setName(garbage.getName());
            detail.setCount(garbage.getCollectionCount());
            detail.setTime(garbage.getCollectionTime());
            detail.setMemoryPoolNames( Arrays.deepToString(garbage.getMemoryPoolNames()));
            details.add(detail);
        }
        return details;
    }
}
