package io.gitee.dqcer.mcdull.uac.client.api;

import io.gitee.dqcer.mcdull.framework.base.constants.HttpHeaderConstants;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录服务api
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface AuthServiceApi {

    /**
     * 验证token
     * todo traceId 待优化
     *
     * @param token   token
     * @param traceId 跟踪id
     * @return {@link Result}<{@link Long}>
     */
    @PostMapping("feign/token/valid")
    Result<Long> tokenValid(@RequestParam(value = "token")String token, @RequestHeader(name = HttpHeaderConstants.TRACE_ID_HEADER) String traceId);

}
