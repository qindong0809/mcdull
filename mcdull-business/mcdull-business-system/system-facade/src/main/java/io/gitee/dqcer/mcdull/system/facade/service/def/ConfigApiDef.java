package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
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
public interface ConfigApiDef {

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM +"/config/list")
    ResultApi<List<RemoteConfigVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/config/by-key/list")
    ResultApi<Map<String, RemoteConfigVO>> getMapByKeyList(@RequestBody List<String> keyList);

}
