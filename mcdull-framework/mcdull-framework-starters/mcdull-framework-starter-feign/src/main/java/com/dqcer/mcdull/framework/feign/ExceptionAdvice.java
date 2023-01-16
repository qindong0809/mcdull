package io.gitee.dqcer.framework.feign;

import io.gitee.dqcer.framework.base.exception.FeignBizException;
import io.gitee.dqcer.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.framework.base.wrapper.Result;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * 全局异常处理程序
 *
 * @author dqcer
 * @version  2021/08/17
 */
@RestControllerAdvice
@Order(-1)
public class ExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 业务异常
     *
     * @param exception 异常
     * @return {@link Result}<{@link ?}>
     */
    @ExceptionHandler(value = FeignException.class)
    public Result<?> feignException(FeignException exception) {
        log.error("feign调用异常: ", exception);
        return Result.error(CodeEnum.FEIGN_BIZ, Collections.singletonList(exception.contentUTF8()));
    }

    /**
     * 业务异常
     *
     * @param exception 异常
     * @return {@link Result}<{@link ?}>
     */
    @ExceptionHandler(value = FeignBizException.class)
    public Result<?> feignBizException(FeignBizException exception) {
        log.error("feign调用异常: ", exception);
        return Result.error(CodeEnum.FEIGN_BIZ, Collections.singletonList(exception.getResult()));
    }

}
