package io.gitee.dqcer.blaze.service;

import io.gitee.dqcer.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.TalentCertificateVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;


/**
 * 证书需求表 业务接口类
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

public interface ITalentCertificateService {

    void insert(TalentCertificateAddDTO dto);

    void update(TalentCertificateUpdateDTO dto);

    void batchDelete(List<Integer> idList);

    PagedVO<TalentCertificateVO> queryPage(TalentCertificateQueryDTO dto);

    void exportData();

    void downloadTemplate();
}
