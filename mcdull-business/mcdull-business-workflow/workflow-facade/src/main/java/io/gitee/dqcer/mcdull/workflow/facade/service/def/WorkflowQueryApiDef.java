package io.gitee.dqcer.mcdull.workflow.facade.service.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.constants.WorkflowConstant;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HistoryBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.LatestApprovalVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 历史与聚合查询契约。
 *
 * @author dqcer
 * @since 2026-11-21
 */
public interface WorkflowQueryApiDef {

    @PostMapping(WorkflowConstant.BASE_PATH + "/query/histories")
    InnerResult<BatchQueryResult<Long, List<HistoryVO>>> batchGetHistoriesByInstanceIds(
            @Valid @RequestBody HistoryBatchQueryDTO dto);

    @PostMapping(WorkflowConstant.BASE_PATH + "/query/latest-approvals")
    InnerResult<BatchQueryResult<ProcessBusinessKey, LatestApprovalVO>> batchGetLatestApprovals(
            @Valid @RequestBody BusinessKeyBatchQueryDTO dto);
}
