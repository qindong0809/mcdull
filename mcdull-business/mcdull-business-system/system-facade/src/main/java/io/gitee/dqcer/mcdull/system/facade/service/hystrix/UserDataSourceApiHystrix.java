package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.service.UserDataSourceApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteUserDataSourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户数据源 Feign 降级：解析失败时返回 hystrix 失败结果。
 * <p>
 * starter-mysql 侧对失败结果按「用户无绑定数据源」处理，降级到默认数据源，
 * 不会因 system 服务不可用而阻断业务服务启动与请求处理。
 *
 * @author dqcer
 */
@Slf4j
@Component
public class UserDataSourceApiHystrix implements UserDataSourceApi {

    @Override
    public InnerResult<RemoteUserDataSourceVO> getByUserId(String userId) {
        LogHelp.error(log, "UserDataSourceApiHystrix#getByUserId: {}", userId);
        return InnerResult.hystrixFallBack();
    }
}
