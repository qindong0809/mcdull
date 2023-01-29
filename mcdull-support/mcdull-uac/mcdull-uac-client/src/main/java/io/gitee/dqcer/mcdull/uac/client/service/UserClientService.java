package io.gitee.dqcer.mcdull.uac.client.service;

import io.gitee.dqcer.mcdull.uac.client.api.UserServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 用户客户端服务
 *
 * @author dqcer
 * @since 2022/11/08
 */
@FeignClient(value = "${mcdull.feign.uac}", contextId = "userClientService")
public interface UserClientService extends UserServiceApi {
}
