package io.gitee.dqcer.mcdull.mdc.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.mdc.client.service.EmailApi;
import org.springframework.stereotype.Component;

/**
 * @author dqcer
 * @since 2024/03/26
 */
@Component
public class EmailApiHystrix implements EmailApi {

    @Override
    public ResultApi<Boolean> sendEmail(String sendTo, String subject, String text) {
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<Boolean> sendEmailWithBytes(byte[] bytes, String fileName, String sendTo, String subject, String text) {
        return ResultApi.error("hystrix fall back!");
    }
}
