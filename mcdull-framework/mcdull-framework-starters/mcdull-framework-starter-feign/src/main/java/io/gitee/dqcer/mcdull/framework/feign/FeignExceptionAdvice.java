package io.gitee.dqcer.mcdull.framework.feign;

import feign.FeignException;
import feign.RetryableException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignBizException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignServiceErrorException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

/**
 * Feign 调用全局异常处理程序
 * <p>
 * 职责：拦截下游调用上游微服务时产生的网络异常、协议异常与未捕获的上游业务异常，
 * 统一转换为对外响应包装类 Result<?>，避免内部技术细节泄露。
 *
 * @author dqcer
 * @since 2021/08/17
 */
// 显式指定 bean 名，避免与 starter-web 中同名的 FeignExceptionAdvice 在
// scanBasePackages = "io.gitee" 的单体模式下产生 bean 名冲突
@RestControllerAdvice("feignExceptionAdvice")
@Order(-1)
public class FeignExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(FeignExceptionAdvice.class);

    /**
     * 1. 拦截 Feign 网络超时 / 连接拒绝异常
     */
    @ExceptionHandler(value = RetryableException.class)
    public Result<?> handleRetryableException(RetryableException exception) {
        String url = exception.request() != null ? exception.request().url() : "unknown";
        log.error("Feign 调用超时或连接异常, url: {}", url, exception);
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList("上游服务响应超时，请稍后重试"));
    }

    /**
     * 2. 拦截 Feign 底层 HTTP 协议异常（如 404, 500, 503 等非 2xx 响应）
     */
    @ExceptionHandler(value = FeignException.class)
    public Result<?> handleFeignException(FeignException exception) {
        String url = exception.request() != null ? exception.request().url() : "unknown";
        String content = exception.contentUTF8();
        log.error("Feign 底层调用异常, status: {}, url: {}, response: {}",
            exception.status(), url, content, exception);

        // 当服务未注册(503)或连接断开时，contentUTF8() 可能为空，提供兜底提示
        String detailMsg = (content != null && !content.isEmpty())
            ? content
            : "上游服务不可用 (HTTP " + exception.status() + ")";
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(detailMsg));
    }

    /**
     * 3. 拦截上游系统业务异常（InnerResult.unwrap() 降级抛出的通用业务异常）
     */
    @ExceptionHandler(value = FeignBizException.class)
    public Result<?> handleFeignBizException(FeignBizException exception) {
        InnerResult<?> innerResult = exception.getResult();
        if (innerResult == null) {
            log.warn("Feign 调用业务异常，且 InnerResult 为空", exception);
            return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList("上游业务处理失败"));
        }

        // 记录详细的调试堆栈（如果上游在非生产环境填充了 exceptionDetail），便于排查
        if (innerResult.getExceptionDetail() != null) {
            log.warn("Feign 上游业务异常, code: {}, message: {}, upstreamTrace:\n{}",
                innerResult.getCode(), innerResult.getMessage(), innerResult.getExceptionDetail());
        } else {
            log.warn("Feign 上游业务异常, code: {}, message: {}",
                innerResult.getCode(), innerResult.getMessage());
        }
        return Result.error(innerResult.getCode(), innerResult.getMessage(), innerResult.getExceptionDetail());
    }

    /**
     * 4. 拦截上游服务契约异常（如返回响应为 null，或要求非空但 data 为 null）
     */
    @ExceptionHandler(value = FeignServiceErrorException.class)
    public Result<?> handleFeignServiceErrorException(FeignServiceErrorException exception) {
        log.error("Feign 上游服务数据契约异常: {}", exception.getMessage(), exception);
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(exception.getMessage()));
    }
}
