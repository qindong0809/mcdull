package io.gitee.dqcer.mcdull.system.client.service.hystrix;

import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.client.service.def.RoleApiDef;
import io.gitee.dqcer.mcdull.system.client.vo.RemoteRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Role Hystrix
 *
 * @author dqcer
 * @since 2025/04/18
 */
@Slf4j
@Component
public class RoleApiHystrix implements RoleApiDef {

    @Override
    public ResultApi<List<RemoteRoleVO>> getList() {
        LogHelp.error(log, "RoleApiHystrix#getList");
        return ResultApi.error("hystrix fall back!");
    }

    @Override
    public ResultApi<Map<Integer, RemoteRoleVO>> getMapByIdList(List<Integer> userIdList) {
        LogHelp.error(log, "RoleApiHystrix#getMapByIdList: {}", userIdList);
        return ResultApi.error("hystrix fall back!");
    }
}
