package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteConfigVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Config Api Def
 * <p>
 * 配置能力契约定义。对外 RPC 入口见 {@link io.gitee.dqcer.mcdull.system.facade.service.ConfigApi}。
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface ConfigApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM +"/config/list")
    InnerResult<List<RemoteConfigVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/config/by-key/list")
    InnerResult<Map<String, RemoteConfigVO>> getMapByKeyList(@RequestBody List<String> keyList);

}
