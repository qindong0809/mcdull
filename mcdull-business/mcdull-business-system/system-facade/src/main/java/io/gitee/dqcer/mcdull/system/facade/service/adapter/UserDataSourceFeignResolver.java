package io.gitee.dqcer.mcdull.system.facade.service.adapter;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.spi.datasource.UserDataSource;
import io.gitee.dqcer.mcdull.framework.spi.datasource.UserDataSourceResolver;
import io.gitee.dqcer.mcdull.system.facade.service.UserDataSourceApi;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Feign → 框架 SPI 适配器：通过 system Feign 查询用户绑定的数据源，
 * 并转换成框架无业务语义的 {@link UserDataSource}。
 * <p>
 * 放在 {@code service.adapter} 包而非业务实现层，定位为 RPC 协议适配组件（与 hystrix 同类），
 * 而非领域逻辑。同时保持 framework 不依赖 business：starter-mysql 只依赖
 * {@link UserDataSourceResolver} SPI，具体由谁实现由部署形态决定。
 * <p>
 * 注意：system-impl 侧的查询接口本阶段尚未实现，Feign 调用会走 hystrix 降级返回失败结果；
 * 本适配器捕获异常后返回 null，框架据此降级到默认数据源，不阻断服务启动与请求处理。
 *
 * @author dqcer
 */
@Component
public class UserDataSourceFeignResolver implements UserDataSourceResolver {

    private static final Logger log = LoggerFactory.getLogger(UserDataSourceFeignResolver.class);

    @Resource
    private UserDataSourceApi userDataSourceApi;

    @Override
    public UserDataSource resolve(String userId) {
        if (userId == null || userId.isBlank()) {
            return null;
        }
        try {
            // RemoteUserDataSourceVO 实现 UserDataSource，无需逐字段搬运
            return userDataSourceApi.getByUserId(userId).unwrapOrNull();
        } catch (Exception e) {
            // Feign 调用失败 / system-impl 尚未实现 / 网络异常：降级到默认数据源
            LogHelp.warn(log, "解析用户绑定数据源失败，降级到默认数据源. userId={}, cause={}",
                    userId, e.getMessage());
            return null;
        }
    }
}
