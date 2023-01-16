package io.gitee.dqcer.mdc.client.service;

import io.gitee.dqcer.mdc.client.api.DictServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * sys dict客户服务
 *
 * @author dqcer
 * @version 2022/11/01 22:11:00
 */
@FeignClient(value = "mcdull-mdc-provider", contextId = "dict")
public interface DictClientService extends DictServiceApi {

}
