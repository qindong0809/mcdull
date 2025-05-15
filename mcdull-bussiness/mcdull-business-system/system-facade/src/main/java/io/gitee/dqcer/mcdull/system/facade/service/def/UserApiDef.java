package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteUserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * User Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface UserApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/list")
    ResultApi<List<RemoteUserVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/user/by-id/list")
    ResultApi<Map<Integer, RemoteUserVO>> getMapByIdList(@RequestBody List<Integer> userIdList);

}
