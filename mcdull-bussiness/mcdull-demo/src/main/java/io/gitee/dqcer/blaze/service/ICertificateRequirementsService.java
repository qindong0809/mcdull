package io.gitee.dqcer.blaze.service;

import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;

import java.util.List;


/**
 * 证书需求表 业务接口类
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

public interface ICertificateRequirementsService {

    void insert(CertificateRequirementsAddDTO dto);

    void update(CertificateRequirementsUpdateDTO dto);

    CertificateRequirementsVO detail(Integer id);

    void batchDelete(List<Integer> idList);

    void delete(Integer id);

    PagedVO<CertificateRequirementsVO> queryPage(CertificateRequirementsQueryDTO dto);
}
