package io.gitee.dqcer.mcdull.mdc.client.service;

import io.gitee.dqcer.mcdull.mdc.client.constants.MdcConstant;
import io.gitee.dqcer.mcdull.mdc.client.service.def.EmailTemplateApiDef;
import io.gitee.dqcer.mcdull.mdc.client.service.hystrix.EmailTemplateApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 邮件发送
 *
 * @author dqcer
 * @since 2022/11/01 22:11:00
 */
@FeignClient(
        value = MdcConstant.SERVICE_NAME,
        contextId = "emailTemplateApi",
        fallback = EmailTemplateApiHystrix.class)
public interface EmailTemplateApi extends EmailTemplateApiDef {

}
