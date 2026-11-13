package io.gitee.dqcer.mcdull.framework.feign.rpc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * RPC 自动装配开关
 * <p>
 * 只有显式配置 {@code mcdull.rpc.mode=remote} 时，才会扫描并激活 Feign 远程调用；
 * 单体合并部署下不做任何 Feign 初始化，全部走本地实现的内存调用。
 * <p>
 * 扫描的包可通过 {@code mcdull.rpc.base-packages} 覆盖，默认只扫描契约包，避免误扫历史遗留的 Feign 客户端。
 *
 * @author dqcer
 * @since 2024/06/12
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "mcdull.rpc", name = "mode", havingValue = "remote")
@EnableFeignClients(basePackages = "${mcdull.rpc.base-packages:io.gitee.dqcer.mcdull.system.facade.service.def}")
public class RpcAutoConfiguration {
}
