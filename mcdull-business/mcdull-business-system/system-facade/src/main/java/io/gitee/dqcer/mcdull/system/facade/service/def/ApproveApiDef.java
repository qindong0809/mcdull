package io.gitee.dqcer.mcdull.system.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.system.facade.model.dto.ApproveDeptQueryDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.ApproveDeptVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 审批人部门回填对外契约（涉及 system 用户/部门表，远程调用）
 */
public interface ApproveApiDef {

    /**
     * 按当前用户与审批人 id 列表批量查询部门信息
     *
     * @return key 为审批人用户 id
     */
    @PostMapping(GlobalConstant.INNER_API + GlobalConstant.SYSTEM + "/approve/dept-info")
    Map<Integer, ApproveDeptVO> deptInfo(@RequestBody ApproveDeptQueryDTO dto);
}
