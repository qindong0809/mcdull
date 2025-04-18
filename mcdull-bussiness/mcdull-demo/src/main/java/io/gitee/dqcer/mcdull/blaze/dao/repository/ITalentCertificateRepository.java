package io.gitee.dqcer.mcdull.blaze.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.blaze.domain.entity.TalentCertificateEntity;
import io.gitee.dqcer.mcdull.blaze.domain.form.TalentCertificateQueryDTO;

import java.util.List;


/**
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
public interface ITalentCertificateRepository extends IService<TalentCertificateEntity> {


    List<TalentCertificateEntity> queryListByIds(List<Integer> idList);

    Page<TalentCertificateEntity> selectPage(TalentCertificateQueryDTO dto);

}
