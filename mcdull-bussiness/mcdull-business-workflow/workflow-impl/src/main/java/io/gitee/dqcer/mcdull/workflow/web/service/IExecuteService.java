package io.gitee.dqcer.mcdull.workflow.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowTaskDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowHisTaskVO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowTaskVO;

public interface IExecuteService {

    PagedVO<FlowTaskVO> toDoPage(FlowTaskDTO dto);

    PagedVO<FlowHisTaskVO> copyPage(FlowTaskDTO dto);
}
