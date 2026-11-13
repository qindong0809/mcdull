package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.feign.rpc.RpcClient;
import io.gitee.dqcer.mcdull.system.facade.model.vo.DepartmentInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
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
@RpcClient("mcdull-system")
public interface DeptApiDef {

    /**
     * 全量部门列表
     */
    @GetMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/dept/all")
    List<DepartmentInfoVO> getAll();

    /**
     * 按 ID 批量查询部门名称
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/dept/name-map")
    Map<Integer, String> getNameMap(@RequestBody List<Integer> idList);

}
