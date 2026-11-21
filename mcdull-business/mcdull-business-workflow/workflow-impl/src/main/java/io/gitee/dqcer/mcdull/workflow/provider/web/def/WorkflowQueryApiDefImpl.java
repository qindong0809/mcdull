package io.gitee.dqcer.mcdull.workflow.provider.web.def;

import io.gitee.dqcer.mcdull.framework.base.wrapper.InnerResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.BatchQueryResult;
import io.gitee.dqcer.mcdull.workflow.facade.model.common.ProcessBusinessKey;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.BusinessKeyBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.dto.HistoryBatchQueryDTO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.HistoryVO;
import io.gitee.dqcer.mcdull.workflow.facade.model.vo.LatestApprovalVO;
import io.gitee.dqcer.mcdull.workflow.facade.service.WorkflowQueryApi;
import io.gitee.dqcer.mcdull.workflow.provider.application.WorkflowApplicationService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 历史与聚合查询 provider。
 *
 * @author dqcer
 * @since 2026-11-21
 */
@Primary
@RestController
public class WorkflowQueryApiDefImpl implements WorkflowQueryApi {

    @Resource
    private WorkflowApplicationService workflowApplicationService;

    @Override
    public InnerResult<BatchQueryResult<Long, List<HistoryVO>>> batchGetHistoriesByInstanceIds(
            HistoryBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetHistoriesByInstanceIds(dto));
    }

    @Override
    public InnerResult<BatchQueryResult<ProcessBusinessKey, LatestApprovalVO>> batchGetLatestApprovals(
            BusinessKeyBatchQueryDTO dto) {
        return InnerResult.success(workflowApplicationService.batchGetLatestApprovals(dto));
    }
}
