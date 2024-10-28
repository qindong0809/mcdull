package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum;

import java.util.Date;

/**
 * Biz audit Service
 *
 * @author dqcer
 * @since 2024/7/25 9:16
 */

public interface IBizAuditService {

    void insert(String bizTypeCode, OperationTypeEnum operationTypeEnum, String bizIndex, Integer bizId, String comment, String operator, Date operationTime, String ext);

}
