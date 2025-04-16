package io.gitee.dqcer.blaze.service;

import cn.hutool.core.lang.Pair;
import io.gitee.dqcer.blaze.domain.vo.ReportViewVO;

/**
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

public interface IReportService {

    ReportViewVO getReportView();

    Integer getMatchSuccessCountTotal();

    Integer getEnterpriseCertificateDemandPendingMatchCount();

    Integer getTalentCertificatePendingMatchCount();

    Pair<Integer, Integer> getEnterpriseTalentCountTotal();
}
