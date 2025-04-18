package io.gitee.dqcer.mcdull.system.client.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.client.vo.RemoteDeptVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Dept Api Def
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface DeptApiDef {

    @PostMapping(GlobalConstant.INNER_API + "/dept/list")
    ResultApi<List<RemoteDeptVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + "/dept/by-id/list")
    ResultApi<Map<Integer, RemoteDeptVO>> getMapByIdList(@RequestBody List<Integer> idList);

}
