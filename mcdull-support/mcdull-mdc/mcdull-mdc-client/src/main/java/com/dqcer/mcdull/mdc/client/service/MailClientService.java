package io.gitee.dqcer.mdc.client.service;

import io.gitee.dqcer.mdc.client.api.MailServiceApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 邮件发送
 *
 * @author dqcer
 * @version 2022/11/01 22:11:00
 */
@FeignClient(value = "mcdull-mdc-provider", contextId = "mail")
public interface MailClientService extends MailServiceApi {

}
