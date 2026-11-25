package io.gitee.dqcer.mcdull.blaze.web.service;

import java.io.IOException;

/**
 * 证书需求与订单明细的跨服务报表编排契约。
 *
 * <p>独立于 CertificateRequirementsService / BlazeOrderService，
 * 避免两个基础服务互相持有富查询依赖形成循环。
 */
public interface ICertificateOrderReportService {

    void getExportCustomerOrderDetailPdf(Integer customerId) throws IOException;
}
