package io.gitee.dqcer.mcdull.workflow.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVersionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import io.gitee.dqcer.mcdull.workflow.facade.service.WorkflowDefinitionApi;
import io.gitee.dqcer.mcdull.workflow.provider.application.WorkflowApplicationService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程定义 provider。单体模式本地直调，微服务模式作为 HTTP 入口。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Primary
@RestController
public class WorkflowDefinitionApiDefImpl implements WorkflowDefinitionApi {

    @Resource
    private WorkflowApplicationService workflowApplicationService;

    @Override
    public InnerResult<BatchQueryResult<String, DefinitionVO>> batchGetDefinitions(DefinitionBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDefinitions(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<String, DefinitionVersionVO>> batchGetDefinitionVersions(DefinitionVersionBatchDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDefinitionVersions(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<String, DiagramVO>> batchGetDiagrams(DiagramBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetDiagrams(dto));
    }
}
