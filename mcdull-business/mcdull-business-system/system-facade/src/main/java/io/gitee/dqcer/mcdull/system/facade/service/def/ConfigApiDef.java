package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.constants.SystemConstant;
import io.gitee.dqcer.mcdull.system.facade.service.hystrix.ConfigApiHystrix;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteConfigVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Config Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
@RpcClient(value = SystemConstant.SERVICE_NAME, contextId = "ConfigApi", fallback = ConfigApiHystrix.class)
public interface ConfigApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM +"/config/list")
    InnerResult<List<RemoteConfigVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/config/by-key/list")
    InnerResult<Map<String, RemoteConfigVO>> getMapByKeyList(@RequestBody List<String> keyList);

}
