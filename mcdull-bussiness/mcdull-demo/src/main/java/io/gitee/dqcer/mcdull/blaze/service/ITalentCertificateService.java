package io.gitee.dqcer.mcdull.blaze.service;

import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateAddDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateQueryDTO;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateUpdateDTO;
import io.gitee.dqcer.mcdull.blaze.domain.vo.TalentCertificateVO;
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

public interface ITalentCertificateService {

    void insert(TalentCertificateAddDTO dto, List<MultipartFile> fileList);

    void update(TalentCertificateUpdateDTO dto, List<MultipartFile> fileList);

    void batchDelete(List<Integer> idList);

    PagedVO<TalentCertificateVO> queryPage(TalentCertificateQueryDTO dto);

    void exportData(TalentCertificateQueryDTO dto);

    void downloadTemplate();

    List<LabelValueVO<Integer, String>> list(Integer customerCertId, boolean isFilter);

    Map<Integer, TalentCertificateEntity> map(Set<Integer> set);

    boolean existByTalentId(Integer talentId);

    void approve(ApproveDTO dto);

    List<LabelValueVO<Integer, String>> getList(Integer customerCertId);

    TalentCertificateEntity get(Integer talentCertId);

    Integer copy(Integer id);
}
