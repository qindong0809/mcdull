package com.dqcer.framework.base.constants;

/**
 * http头部常量
 *
 * @author dqcer
 * @date 2022/07/26
 */
@SuppressWarnings("unused")
public class HttpHeaderConstants {


    /**
     * 禁止实例化
     */
    private HttpHeaderConstants(){
        throw new AssertionError();
    }


    /**
     * 授权key
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * Bearer
     */
    public static final String BEARER = "Bearer ";

    /**
     * 租户id
     */
    public static final String T_ID = "tid";

    /**
     * 用户id
     */
    public static final String U_ID = "uid";

    /**
     * traceId
     */
    public static final String TRACE_ID = "traceId";


}
