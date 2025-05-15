package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.system.facade.vo.RemoteDeptVO;
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

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/dept/list")
    ResultApi<List<RemoteDeptVO>> getList();

    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/dept/by-id/list")
    ResultApi<Map<Integer, RemoteDeptVO>> getMapByIdList(@RequestBody List<Integer> idList);

}
