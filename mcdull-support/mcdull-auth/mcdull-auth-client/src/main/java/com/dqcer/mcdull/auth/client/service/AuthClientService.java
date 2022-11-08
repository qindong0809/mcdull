package com.dqcer.mcdull.auth.client.service;

import com.dqcer.mcdull.auth.client.api.AuthServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 身份验证客户服务
 *
 * @author QinDong
 * @date 2022/11/08
 */
@FeignClient(value = "mcdull-auth-provider")
public interface AuthClientService extends AuthServiceApi {
}
