package com.dqcer.mcdull.uac.client.service;

import com.dqcer.mcdull.uac.client.api.AuthServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 身份验证客户服务
 *
 * @author dqcer
 * @version 2022/11/08
 */
@FeignClient(value = "${mcdull.feign.uac}")
public interface AuthClientService extends AuthServiceApi {
}
