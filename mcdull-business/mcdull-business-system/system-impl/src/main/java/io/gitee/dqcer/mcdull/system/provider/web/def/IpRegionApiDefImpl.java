package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.service.IpRegionApi;
import io.gitee.dqcer.mcdull.system.provider.util.Ip2RegionUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * IP 归属地能力实现。
 * <p>
 * {@code @Primary}：单体模式下若同时存在本地实现与 Feign 代理，强制优先注入本地实现。
 * {@code @RestController}：微服务模式下自动把契约暴露为标准 HTTP 接口供 Feign 调用。
 *
 * @author dqcer
 * @since 2026-11-22
 */
@Primary
@RestController
public class IpRegionApiDefImpl implements IpRegionApi {

    @Override
    public InnerResult<String> getRegion(String ip) {
        return InnerResult.success(Ip2RegionUtil.getRegion(ip));
    }

    @Override
    public InnerResult<Map<String, String>> getRegionMap(List<String> ipList) {
        Map<String, String> result = new LinkedHashMap<>();
        if (ipList != null) {
            for (String ip : ipList) {
                result.put(ip, Ip2RegionUtil.getRegion(ip));
            }
        }
        return InnerResult.success(result);
    }
}
