package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteRoleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Role Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface RoleApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/role/list")
    ResultApi<List<RemoteRoleVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/role/by-id/list")
    ResultApi<Map<Integer, RemoteRoleVO>> getMapByIdList(@RequestBody List<Integer> userIdList);

}
