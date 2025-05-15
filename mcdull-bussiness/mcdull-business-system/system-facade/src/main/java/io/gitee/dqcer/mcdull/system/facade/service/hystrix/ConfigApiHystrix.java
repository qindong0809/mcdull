package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.ConfigApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ConfigApiHystrix implements ConfigApiDef {

    @Override
    public ResultApi<List<RemoteConfigVO>> getList() {
        LogHelp.error(log, "ConfigApiHystrix#getList");
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<Map<String, RemoteConfigVO>> getMapByKeyList(List<String> keyList) {
        LogHelp.error(log, "ConfigApiHystrix#getMapByKeyList: {}", keyList);
        return ResultApi.error("hystrix fall back!");
    }
}
