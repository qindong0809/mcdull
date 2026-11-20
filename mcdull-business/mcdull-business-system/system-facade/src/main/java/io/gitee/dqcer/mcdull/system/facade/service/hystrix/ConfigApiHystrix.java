package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.system.facade.service.ConfigApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ConfigApiHystrix implements ConfigApi {

    @Override
    public InnerResult<List<RemoteConfigVO>> getList() {
        LogHelp.error(log, "ConfigApiHystrix#getList");
        return InnerResult.hystrixFallBack();
    }

    @Override
    public InnerResult<Map<String, RemoteConfigVO>> getMapByKeyList(List<String> keyList) {
        LogHelp.error(log, "ConfigApiHystrix#getMapByKeyList: {}", keyList);
        return InnerResult.hystrixFallBack();
    }
}
