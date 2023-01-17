package io.gitee.dqcer.mcdull.uac.client.service;

import io.gitee.dqcer.mcdull.uac.client.api.AuthServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 身份验证客户服务
 *
 * @author dqcer
 * @version 2022/11/08
 */
@FeignClient(value = "${mcdull.feign.uac}", contextId = "authClientService")
public interface AuthClientService extends AuthServiceApi {
}
