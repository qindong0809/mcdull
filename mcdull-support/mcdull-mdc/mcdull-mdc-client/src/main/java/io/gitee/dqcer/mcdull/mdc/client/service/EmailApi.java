package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.service.def.EmailApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.EmailApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 邮件发送
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = "${api:mdcName}",
        contextId = "emailClientService",
        fallback = EmailApiHystrix.class)
public interface EmailApi extends EmailApiDef {

}
