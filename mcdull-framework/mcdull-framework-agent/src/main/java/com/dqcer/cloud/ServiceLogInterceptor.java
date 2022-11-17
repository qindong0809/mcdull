package com.dqcer.cloud;

import cn.hutool.core.util.IdUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dqcer.cloud.model.ServiceGcLog;
import com.dqcer.cloud.model.ServiceLog;
import net.bytebuddy.implementation.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 时间拦截器
 *
 * @author dqcer
 * @version  2022/11/10
 */
public class ServiceLogInterceptor {


    public static final IdWorker logId = new IdWorker(1,1,1);
    public static final IdWorker idWorker = new IdWorker(2,2,2);

    private static final Logger log = LoggerFactory.getLogger(McdullListener.class);

    private Db use;
    public ServiceLogInterceptor(Db use) {
        this.use = use;
    }

    /**
     * 拦截方法
     *
     * @param method 拦截的方法
     * @param callable 调用对象的代理对象
     * @param args               参数
     * @return {@link Object}
     * @throws Exception 异常
     */
    @SuppressWarnings("unused")
    @RuntimeType
    public Object intercept(@This Object obj,  @AllArguments Object[] args, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        // todo 需要获取traceId 才能进行上下文关联
        log.info("obj: {}", obj.getClass().getName());

        long start = System.currentTimeMillis();
        Integer status = 1;
        Object result = null;
        String respResult = null;
        ServiceLog memoryInfo = null;
        List<ServiceGcLog> gcInfos = new ArrayList<>();
        try {
            memoryInfo = JvmInfoUtil.beforeMemoryInfo();
            gcInfos = JvmInfoUtil.getGCInfo(memoryInfo.getId(), 1);

            // 原有函数执行
            result = callable.call();

            respResult = JSONObject.toJSONString(result, SerializerFeature.IgnoreErrorGetter);

        } catch (Exception e){
            log.error(e.getMessage(),e);
            status = 2;
            respResult = e.getMessage();
        } finally{
            memoryInfo = JvmInfoUtil.afterMemoryInfo(memoryInfo);
            gcInfos.addAll(JvmInfoUtil.getGCInfo(memoryInfo.getId(), 2));

            long endTime = System.currentTimeMillis();
            saveLog(obj, args, method, status, respResult, start, endTime, memoryInfo, gcInfos);
        }
        return result;
    }

    /**
     * 保存日志
     *
     * @param args       参数
     * @param method     方法
     * @param status     状态
     * @param respResult 返回结果
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param memoryInfo
     * @param gcInfos
     */
    private void saveLog(Object obj, Object[] args, Method method, Integer status, String respResult, long startTime, long endTime, ServiceLog memoryInfo, List<ServiceGcLog> gcInfos) {
        ServiceLog serviceLog = getBaseInfoArgs(memoryInfo, args,method);
        serviceLog.setClassName(serviceLog.getClassName() + obj.getClass().getName());
        Date date = new Date();
        date.setTime(startTime);
        serviceLog.setCreatedTime(date);
        Date endDate = new Date();
        endDate.setTime(endTime);
        serviceLog.setEndTime(endDate);
        serviceLog.setCostTime(endTime - startTime);
        serviceLog.setResult(respResult);
        serviceLog.setStatus(status);
        serviceLog.setTraceId(MDC.get("traceId"));

        log.info("Agent: {}, GC: {}", JSONObject.toJSONString(serviceLog, SerializerFeature.IgnoreErrorGetter), JSONArray.toJSONString(gcInfos));
        try {
            saveDB(serviceLog, gcInfos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取基础参数值
     *
     * @param args   参数集
     * @param method 方法
     * @return {@link ServiceLog}
     */
    public ServiceLog getBaseInfoArgs(ServiceLog serviceLog, Object[] args, Method method){
        serviceLog.setMethodName(method.getName());
        serviceLog.setReqArgs(JSONObject.toJSONString(args, SerializerFeature.IgnoreErrorGetter));
        serviceLog.setClassName(method.getDeclaringClass().getName());
        return serviceLog;
    }

    private void saveDB(ServiceLog log, List<ServiceGcLog> gcLogs) throws SQLException {
        // TODO: 2022/11/16 暂定
        long id = logId.nextId();
        use.insert(Entity.create("service_log")
                .set("id", id)
                .set("trace_id", UUID.randomUUID().toString())
                .set("class_name", log.getClassName())
                .set("method_name", log.getMethodName())
                .set("req_args", log.getReqArgs())
                .set("result", log.getResult())
                .set("cost_time", log.getCostTime())
                .set("status", log.getStatus())
                .set("created_time", log.getCreatedTime())
                .set("end_time", log.getEndTime())
                .set("before_head_memory_init", log.getBeforeHeadMemoryInit())
                .set("before_head_memory_used", log.getBeforeHeadMemoryUsed())
                .set("before_head_memory_committed", log.getBeforeHeadMemoryCommitted())
                .set("before_head_memory_max", log.getBeforeHeadMemoryMax())
                .set("before_head_rate", log.getBeforeHeadRate())
                .set("before_non_head_memory_init", log.getBeforeNonHeadMemoryInit())
                .set("before_non_head_memory_used", log.getBeforeNonHeadMemoryUsed())
                .set("before_non_head_memory_committed", log.getBeforeNonHeadMemoryCommitted())
                .set("before_non_head_memory_max", log.getBeforeNonHeadMemoryMax())
                .set("before_non_head_rate", log.getBeforeNonHeadRate())
                .set("cost_head_memory_init", log.getCostHeadMemoryInit())
                .set("cost_head_memory_used", log.getCostHeadMemoryUsed())
                .set("cost_head_memory_committed", log.getCostHeadMemoryCommitted())
                .set("cost_head_memory_max", log.getCostHeadMemoryMax())
                .set("cost_head_rate", log.getCostHeadRate())
                .set("cost_non_head_memory_init", log.getCostNonHeadMemoryInit())
                .set("cost_non_head_memory_used", log.getCostNonHeadMemoryUsed())
                .set("cost_non_head_memory_committed", log.getCostNonHeadMemoryCommitted())
                .set("cost_non_head_memory_max", log.getCostNonHeadMemoryMax())
                .set("cost_non_head_rate", log.getCostNonHeadRate())
        );

        for (ServiceGcLog gcLog : gcLogs) {
            use.insert(Entity.create("service_gc_log")
                    .set("id", idWorker.nextId())
                    .set("service_log_id", id)
                    .set("name", gcLog.getName())
                    .set("memory_pool_names", gcLog.getMemoryPoolNames())
                    .set("count", gcLog.getCount())
                    .set("time", gcLog.getTime())
                    .set("type", gcLog.getType())
            );
        }
    }

}
