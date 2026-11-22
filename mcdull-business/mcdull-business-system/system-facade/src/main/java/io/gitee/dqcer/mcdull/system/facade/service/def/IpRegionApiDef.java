package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * IP 归属地契约定义。
 * <p>
 * 对外 RPC 入口见 {@link io.gitee.dqcer.mcdull.system.facade.service.IpRegionApi}；
 * 单体模式走 {@code IpRegionApiDefImpl} 本地实现，微服务模式由 IpRegionApi 注册 Feign 代理。
 *
 * @author dqcer
 * @since 2026-11-22
 */
public interface IpRegionApiDef {

    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/ip-region/region")
    InnerResult<String> getRegion(@RequestParam("ip") String ip);

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/ip-region/region/map")
    InnerResult<Map<String, String>> getRegionMap(@RequestBody List<String> ipList);

}
