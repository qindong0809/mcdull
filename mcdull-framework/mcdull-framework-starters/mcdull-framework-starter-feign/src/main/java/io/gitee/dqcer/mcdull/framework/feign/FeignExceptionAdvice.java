package io.gitee.dqcer.mcdull.framework.feign;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;
import feign.RetryableException;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignBizException;
import io.gitee.dqcer.mcdull.framework.base.exception.FeignServiceErrorException;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 统一 RPC / Feign 双向全局异常处理器
 * <p>
 * 1. 当当前请求为内部 RPC 调用（/inner/**）时：作为服务端拦截器，捕获所有异常（含下游传递上来的 Feign 异常），
 *    统一封装为 InnerResult<?> 并填充 exceptionClass 与环境隔离的堆栈摘要，实现多级无损透传。
 * 2. 当当前请求为对外接口（非 /inner/**）时：作为客户端拦截器，捕获调用下游产生的 Feign 异常，
 *    统一转换为对外标准响应 Result<?>；普通 BusinessException 则放行交由 starter-web 主拦截器处理。
 *
 * @author dqcer
 * @since 2021/08/17
 */
@RestControllerAdvice("feignExceptionAdvice")
@Order(-1)
public class FeignExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(FeignExceptionAdvice.class);

    private static final List<String> DEBUG_PROFILES = Arrays.asList("local", "dev", "test", "sit");

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * 1. 拦截 Feign 网络超时 / 连接拒绝异常
     */
    @ExceptionHandler(value = RetryableException.class)
    public Object handleRetryableException(RetryableException exception) {
        String url = exception.request() != null ? exception.request().url() : "unknown";
        log.error("Feign 调用超时或连接异常, url: {}", url, exception);
        String msg = "上游服务响应超时，请稍后重试";

        if (isInnerRequest()) {
            return InnerResult.error(FeignCodeEnum.FEIGN_BIZ.getCode(), msg, exception, extractDebugDetail(exception));
        }
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(msg));
    }

    /**
     * 2. 拦截 Feign 底层 HTTP 协议异常（如 404, 500, 503 等非 2xx 响应）
     */
    @ExceptionHandler(value = FeignException.class)
    public Object handleFeignException(FeignException exception) {
        String url = exception.request() != null ? exception.request().url() : "unknown";
        String content = exception.contentUTF8();
        log.error("Feign 底层调用异常, status: {}, url: {}, response: {}",
            exception.status(), url, content, exception);

        String detailMsg = StrUtil.isNotBlank(content)
            ? content
            : "上游服务不可用 (HTTP " + exception.status() + ")";

        if (isInnerRequest()) {
            return InnerResult.error(FeignCodeEnum.FEIGN_BIZ.getCode(), detailMsg, exception, extractDebugDetail(exception));
        }
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(detailMsg));
    }

    /**
     * 3. 拦截上游系统业务异常（InnerResult.unwrap() 降级抛出的通用业务异常）
     */
    @ExceptionHandler(value = FeignBizException.class)
    public Object handleFeignBizException(FeignBizException exception) {
        InnerResult<?> innerResult = exception.getResult();
        if (innerResult == null) {
            log.warn("Feign 调用业务异常，且 InnerResult 为空", exception);
            if (isInnerRequest()) {
                return InnerResult.error(FeignCodeEnum.FEIGN_BIZ.getCode(), "上游业务处理失败", exception, extractDebugDetail(exception));
            }
            return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList("上游业务处理失败"));
        }

        if (innerResult.getExceptionDetail() != null) {
            log.warn("Feign 上游业务异常, code: {}, message: {}, upstreamTrace:\n{}",
                innerResult.getCode(), innerResult.getMessage(), innerResult.getExceptionDetail());
        } else {
            log.warn("Feign 上游业务异常, code: {}, message: {}",
                innerResult.getCode(), innerResult.getMessage());
        }

        // 若处于多级 RPC 调用中间节点，原样保留异常元数据并继续封装为 InnerResult 向上透传
        if (isInnerRequest()) {
            return InnerResult.builder()
                .withCode(innerResult.getCode())
                .withMessage(innerResult.getMessage())
                .withExceptionClass(innerResult.getExceptionClass())
                .withExceptionDetail(innerResult.getExceptionDetail())
                .build();
        }
        return Result.error(innerResult.getCode(), innerResult.getMessage(), innerResult.getExceptionDetail());
    }

    /**
     * 4. 拦截上游服务契约异常（如返回响应为 null，或要求非空但 data 为 null）
     */
    @ExceptionHandler(value = FeignServiceErrorException.class)
    public Object handleFeignServiceErrorException(FeignServiceErrorException exception) {
        log.error("Feign 上游服务数据契约异常: {}", exception.getMessage(), exception);
        if (isInnerRequest()) {
            return InnerResult.error(FeignCodeEnum.FEIGN_BIZ.getCode(), exception.getMessage(), exception, extractDebugDetail(exception));
        }
        return Result.error(FeignCodeEnum.FEIGN_BIZ, Collections.singletonList(exception.getMessage()));
    }

    /**
     * 5. 拦截业务异常（仅当当前请求为 /inner/** 时拦截并封装为 InnerResult；对外请求直接放行给 starter-web 主拦截器）
     */
    @ExceptionHandler(value = BusinessException.class)
    public InnerResult<?> handleBusinessException(BusinessException exception) throws BusinessException {
        if (!isInnerRequest()) {
            throw exception;
        }
        log.warn("内部 RPC 接口业务异常, code: {}, message: {}", exception.getCode(), exception.getMessage());
        return InnerResult.error(
            exception.getCode().getCode(),
            exception.getMessage(),
            exception,
            extractDebugDetail(exception)
        );
    }

    /**
     * 6. 兜底拦截未预期的系统异常（仅针对 /inner/** 内部接口；对外请求直接放行给 starter-web 主拦截器）
     */
    @ExceptionHandler(value = Exception.class)
    public InnerResult<?> handleException(Exception exception) throws Exception {
        if (!isInnerRequest()) {
            throw exception;
        }
        log.error("内部 RPC 接口发生未预期系统异常: ", exception);
        String msg = StrUtil.isNotBlank(exception.getMessage()) ? exception.getMessage() : "上游服务内部系统异常";
        return InnerResult.error(
            CodeEnum.INTERNAL_SERVER_ERROR.getCode(),
            msg,
            exception,
            extractDebugDetail(exception)
        );
    }

    /**
     * 判断当前请求是否为内部 RPC 接口调用（/inner/ 开头）
     */
    private boolean isInnerRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StrUtil.isNotBlank(contextPath) && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        return uri.startsWith(GlobalConstant.INNER_API);
    }

    /**
     * 仅在非生产环境截取前 800 个字符的异常堆栈摘要
     */
    private String extractDebugDetail(Throwable throwable) {
        if (throwable == null || !isDebugEnv()) {
            return null;
        }
        return ExceptionUtil.stacktraceToString(throwable, 800);
    }

    private boolean isDebugEnv() {
        if (StrUtil.isBlank(activeProfile)) {
            return false;
        }
        for (String profile : activeProfile.split(",")) {
            if (DEBUG_PROFILES.contains(profile.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
