package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.service.IpRegionApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * IP 归属地远程调用降级。
 *
 * @author dqcer
 * @since 2026-11-22
 */
@Slf4j
@Component
public class IpRegionApiFallback implements IpRegionApi {

    @Override
    public InnerResult<String> getRegion(String ip) {
        LogHelp.error(log, "IpRegionApiFallback#getRegion: {}", ip);
        return InnerResult.success("");
    }

    @Override
    public InnerResult<Map<String, String>> getRegionMap(List<String> ipList) {
        LogHelp.error(log, "IpRegionApiFallback#getRegionMap: {}", ipList);
        return InnerResult.success(Collections.<String, String>emptyMap());
    }
}
