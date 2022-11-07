package com.dqcer.mcdull.auth.client.service;

import com.dqcer.mcdull.auth.client.api.AuthServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "mcdull-auth-provider")
public interface AuthClientService extends AuthServiceApi {
}
