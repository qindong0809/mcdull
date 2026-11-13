package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.model.vo.RoleVO;
import org.springframework.web.bind.annotation.GetMapping;
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
@RpcClient("mcdull-system")
public interface RoleApiDef {

    /**
     * 全量角色
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/role/all")
    List<RoleVO> all();

    /**
     * 按用户 ID 查角色列表
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/role/map-by-user")
    Map<Integer, List<RoleVO>> getRoleMap(@RequestBody List<Integer> userIdList);

}
