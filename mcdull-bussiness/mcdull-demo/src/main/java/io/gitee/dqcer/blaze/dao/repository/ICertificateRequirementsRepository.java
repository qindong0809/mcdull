package io.gitee.dqcer.blaze.dao.repository;

import io.gitee.dqcer.blaze.domain.entity.CertificateRequirementsEntity;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.blaze.domain.form.CertificateRequirementsQueryDTO;


/**
 * 证书需求表 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */
public interface ICertificateRequirementsRepository extends IService<CertificateRequirementsEntity> {


    List<CertificateRequirementsEntity> queryListByIds(List<Integer> idList);

    Page<CertificateRequirementsEntity> selectPage(CertificateRequirementsQueryDTO dto);

}
