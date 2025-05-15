package io.gitee.dqcer.mcdull.system.facade.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.service.def.UserApiDef;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * User Hystrix
 *
 * @author dqcer
 * @since 2025/04/18
 */
@Slf4j
@Component
public class UserApiHystrix implements UserApiDef {

    @Override
    public ResultApi<List<RemoteUserVO>> getList() {
        LogHelp.error(log, "UserApiHystrix#getList");
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<Map<Integer, RemoteUserVO>> getMapByIdList(List<Integer> userIdList) {
        LogHelp.error(log, "UserApiHystrix#getMapByIdList: {}", userIdList);
        return ResultApi.error("hystrix fall back!");
    }
}
