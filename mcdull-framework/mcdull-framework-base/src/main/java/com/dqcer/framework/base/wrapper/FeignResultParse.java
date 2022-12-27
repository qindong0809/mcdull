package com.dqcer.framework.base.wrapper;

import com.dqcer.framework.base.exception.FeignBizException;
import com.dqcer.framework.base.exception.FeignServiceErrorException;
import com.dqcer.framework.base.util.ObjUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * feign结果解析
 *
 * @author dongqin
 * @version 2022/04/22
 */
public class FeignResultParse {

    public final static Logger log = LoggerFactory.getLogger(FeignResultParse.class);

    /**
     * 解析result并获取实例，并处理null值
     *
     * @param result 结果
     * @return {@link T}
     * @throws FeignServiceErrorException,FeignBizException 上游系统业务异常，亦可自定义捕获，默认全局拦截
     */
    public static <T> T getInstance(Result<T> result) {
        return getInstance(result, false);
    }

    /**
     * 解析result并获取实例
     *
     * @param result     结果
     * @param ignoreNull 忽略空  true/客户端需考虑npe false/直接跑错
     * @return {@link T}
     * @throws FeignServiceErrorException,FeignBizException 上游系统业务异常，亦可自定义捕获，默认全局拦截
     */
    public static <T> T getInstance(Result<T> result, boolean ignoreNull) {
        if (log.isDebugEnabled()) {
            log.debug("feign result: result:{}", result);
        }
        if(result == null){
            throw new FeignServiceErrorException("上游服务异常...");
        }
        if(result.getCode() != 0){
            log.error("feign调用时，上游系统业务发生异常 code: {}, message: {}", result.getCode(), result.getMessage());
            throw new FeignBizException(result);
        }
        T data = result.getData();
        if (ignoreNull) {
            return data;
        }

        if (ObjUtil.isNotNull(data)) {
            return data;
        }
        throw new FeignServiceErrorException("上游服务数据返回为null");
    }
}
