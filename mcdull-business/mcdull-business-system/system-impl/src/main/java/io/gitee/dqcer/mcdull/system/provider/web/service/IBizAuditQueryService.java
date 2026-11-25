package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.BizAuditVO;

/**
 * 业务审计查询契约。
 *
 * <p>与写入契约分离：查询侧允许注入 User / Menu / CommonManager 补齐展示数据，
 * 写入侧保持窄依赖，供所有 CRUD Service 注入而不产生反向依赖。
 */
public interface IBizAuditQueryService {

    PagedVO<BizAuditVO> queryPage(BizAuditQueryDTO queryForm);

    boolean exportData(BizAuditQueryDTO dto);
}
