package io.gitee.dqcer.blaze.service;

import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsAddDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsUpdateDTO;
import io.gitee.dqcer.blaze.domain.vo.CertificateRequirementsVO;
import io.gitee.dqcer.mcdull.framework.base.dto.ApproveDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 证书需求表 业务接口类
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

public interface ICertificateRequirementsService {

    CertificateRequirementsEntity get(Integer id);

    void insert(CertificateRequirementsAddDTO dto, List<MultipartFile> fileList);

    void update(CertificateRequirementsUpdateDTO dto, List<MultipartFile> fileList);

    void batchDelete(List<Integer> idList);

    PagedVO<CertificateRequirementsVO> queryPage(CertificateRequirementsQueryDTO dto);

    void exportData(CertificateRequirementsQueryDTO dto);

    void downloadTemplate();

    List<LabelValueVO<Integer, String>> all(boolean isFilter);

    Map<Integer, CertificateRequirementsEntity> map(Set<Integer> set);

    boolean existByCustomerId(Integer customerId);

    void approve(ApproveDTO dto);

    List<LabelValueVO<Integer, String>> okList();

    List<LabelValueVO<Integer, String>> getList(Integer talentCertId);
}
