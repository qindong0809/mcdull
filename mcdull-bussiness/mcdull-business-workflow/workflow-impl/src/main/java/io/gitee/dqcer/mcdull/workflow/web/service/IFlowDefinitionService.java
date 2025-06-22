package io.gitee.dqcer.mcdull.workflow.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.workflow.model.dto.FlowDefinitionDTO;
import io.gitee.dqcer.mcdull.workflow.model.vo.FlowDefinitionVO;

import java.io.InputStream;
import java.util.List;

public interface IFlowDefinitionService {

    PagedVO<FlowDefinitionVO> getList(FlowDefinitionDTO dto);

    FlowDefinitionVO getDetail(Long id);

    Boolean add(FlowDefinitionDTO flowDefinition);

    Boolean publish(Long id);

    void unPublish(Long id);

    Boolean updateById(FlowDefinitionDTO flowDefinition);

    Boolean removeDef(List<Long> ids);

    Boolean copyDef(Long id);

    void importIs(InputStream inputStream);
}
