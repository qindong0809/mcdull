package io.gitee.dqcer.mcdull.framework.base.wrapper;

import io.gitee.dqcer.mcdull.framework.base.exception.FeignBizException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignServiceErrorException;
import io.gitee.dqcer.mcdull.framework.base.util.ObjUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * feign结果解析
 *
 * @author dqcer
 * @since 2022/04/22
 */
public class FeignResultParse {

    public final static Logger log = LoggerFactory.getLogger(FeignResultParse.class);

    /**
     * 获得实例，支持函数式但不推荐使用
     * eg:
     *         Result<String> result = Result.error(CodeEnum.DATA_EXIST);
     *         String instance = getInstance(result, Result::getData);
     *         System.out.println(instance);
     *
     * @param result   结果
     * @param function 函数
     * @return {@link T}
     */
    public static <T> T getInstance(Result<T> result, Function<Result<T>, T> function) {
        return function.apply(result);
    }


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
