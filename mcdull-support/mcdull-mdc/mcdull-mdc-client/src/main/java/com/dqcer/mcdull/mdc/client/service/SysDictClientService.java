package com.dqcer.mcdull.mdc.client.service;

import com.dqcer.mcdull.mdc.client.api.SysDictServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * sys dict客户服务
 *
 * @author dqcer
 * @version 2022/11/01 22:11:00
 */
@FeignClient(value = "mcdull-mdc-provider")
public interface SysDictClientService extends SysDictServiceApi {

}
