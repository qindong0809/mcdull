package com.dqcer.cloud;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dqcer.cloud.model.ServiceLog;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * 时间拦截器
 *
 * @author dqcer
 * @version  2022/11/10
 */
public class ServiceLogInterceptor {

    private static final Logger log = LoggerFactory.getLogger(McdullListener.class);

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
    public Object intercept(@AllArguments Object[] args, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        // todo 需要获取traceId 才能进行上下文关联

        long start = System.currentTimeMillis();
        Integer status = 1;
        Object result = null;
        String respResult = null;
        try {
            // 原有函数执行
            result = callable.call();
            respResult = JSONObject.toJSONString(result, SerializerFeature.IgnoreErrorGetter);
        } catch (Exception e){
            log.error(e.getMessage(),e);
            status = 2;
            respResult = e.getMessage();
        } finally{
            long endTime = System.currentTimeMillis();
            saveLog(args, method, status, respResult, start, endTime);
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
     */
    private void saveLog(Object[] args, Method method, Integer status, String respResult, long startTime, long endTime) {
        ServiceLog serviceLog = createServiceLog(args,method);
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
        log.info("Agent: {}", JSONObject.toJSONString(serviceLog, SerializerFeature.IgnoreErrorGetter));
    }

    /**
     * 创建服务日志
     *
     * @param args   参数集
     * @param method 方法
     * @return {@link ServiceLog}
     */
    public ServiceLog createServiceLog(Object[] args, Method method){
        ServiceLog serviceLog = new ServiceLog();
        serviceLog.setMethodName(method.getName());
        serviceLog.setReqArgs(JSONObject.toJSONString(args, SerializerFeature.IgnoreErrorGetter));
        serviceLog.setClassName(method.getDeclaringClass().getName());
        return serviceLog;
    }

}
