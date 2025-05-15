package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.DeptApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteDeptVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeptApiHystrix implements DeptApiDef {

    @Override
    public ResultApi<List<RemoteDeptVO>> getList() {
        LogHelp.error(log, "DeptApiHystrix#getList");
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<Map<Integer, RemoteDeptVO>> getMapByIdList(List<Integer> idList) {
        LogHelp.error(log, "DeptApiHystrix#getMapByIdList: {}", idList);
        return ResultApi.error("hystrix fall back!");
    }
}
