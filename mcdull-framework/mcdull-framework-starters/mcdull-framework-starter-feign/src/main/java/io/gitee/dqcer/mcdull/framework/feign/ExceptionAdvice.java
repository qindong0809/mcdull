package io.gitee.dqcer.mcdull.framework.feign;

import feign.FeignException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * 全局异常处理程序
 *
 * @author dqcer
 * @since  2021/08/17
 */
// 显式指定 bean 名，避免与 starter-web 中同名的 ExceptionAdvice 在
// scanBasePackages = "io.gitee" 的单体模式下产生 bean 名冲突
@Component("feignExceptionAdvice")
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
        // FIXME: 2024/3/26 国际化处理
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(exception.contentUTF8()));
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
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(exception.getResult()));
    }

}
