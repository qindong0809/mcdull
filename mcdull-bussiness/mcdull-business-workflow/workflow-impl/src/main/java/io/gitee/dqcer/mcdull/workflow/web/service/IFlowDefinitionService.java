package io.gitee.dqcer.mcdull.workflow.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowDefinitionDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowDefinitionVO;

public interface IFlowDefinitionService {

    PagedVO<FlowDefinitionVO> getList(FlowDefinitionDTO dto);
}
