package io.gitee.dqcer.mcdull.workflow.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DefinitionVersionBatchDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.DiagramBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DefinitionVersionVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.DiagramVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 流程定义契约。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowDefinitionApiDef {

    @PostMapping(WorkflowConstant.BASE_PATH + "/definitions/batch-get")
    InnerResult<BatchQueryResult<String, DefinitionVO>> batchGetDefinitions(
            @Valid @RequestBody DefinitionBatchQueryDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/definitions/versions")
    InnerResult<BatchQueryResult<String, DefinitionVersionVO>> batchGetDefinitionVersions(
            @Valid @RequestBody DefinitionVersionBatchDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/definitions/diagrams")
    InnerResult<BatchQueryResult<String, DiagramVO>> batchGetDiagrams(
            @Valid @RequestBody DiagramBatchQueryDTO dto);
}
