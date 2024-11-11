package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.BizAuditQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.BizAuditVO;

import java.util.Date;

/**
 * Biz audit Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IBizAuditService {

    /**
     * 插入
     *
     * @param bizTypeCode       业务类型代码
     * @param operationTypeEnum 操作类型 enum
     * @param bizIndex          bizIndex
     * @param bizId             业务 ID
     * @param comment           comment
     * @param operator          operator
     * @param operationTime     操作时间
     * @param ext               ext
     */
    void insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId, String comment, String operator, Date operationTime, String ext);

    /**
     * Query 页面
     *
     * @param queryForm 查询表单
     * @return {@link PagedVO }<{@link BizAuditVO }>
     */
    PagedVO<BizAuditVO> queryPage(BizAuditQueryDTO queryForm);

    /**
     * 导出数据
     *
     * @param dto DTO
     */
    void exportData(BizAuditQueryDTO dto);
}
