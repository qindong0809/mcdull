package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.api.EmailServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 邮件发送
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(value = "mcdull-mdc-provider", contextId = "mail")
public interface EmailClientService extends EmailServiceApi {

}
