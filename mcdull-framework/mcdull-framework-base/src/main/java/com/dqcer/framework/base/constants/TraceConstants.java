package com.dqcer.framework.base.constants;

/**
 * 全局常量
 * 
 * @author dqcer
 * @date 2022/01/25
 */
@SuppressWarnings("unused")
public class TraceConstants {


    /**
     * 禁止实例化
     */
    private TraceConstants(){
        throw new AssertionError();
    }


    /**
     * 跟踪id头
     * eg: http.addHeaders(TRACE_ID_HEADER, "uuid");
     */
    public static final String TRACE_ID_HEADER = "x-trace-header";

    /**
     * 日志跟踪id
     * eg: MDC.put(LOG_TRACE_ID, "uuid);
     */
    public static final String LOG_TRACE_ID = "trace";
}
