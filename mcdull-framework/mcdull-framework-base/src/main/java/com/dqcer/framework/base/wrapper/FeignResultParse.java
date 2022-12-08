package com.dqcer.framework.base.wrapper;

import com.dqcer.framework.base.exception.FeignBizException;
import com.dqcer.framework.base.exception.FeignServiceErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * feign结果解析
 *
 * @author dongqin
 * @date 2022/04/22
 */
public class FeignResultParse {

    public final static Logger log = LoggerFactory.getLogger(FeignResultParse.class);

    /**
     * 获取实例
     *
     * @param result 结果 parse
     * @return {@link T}
     * @throws FeignServiceErrorException,FeignBizException 上游系统业务异常，亦可自定义捕获，默认全局拦截
     */
    public static <T> T getInstance(Result<T> result) {
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
        return result.getData();
    }
}
