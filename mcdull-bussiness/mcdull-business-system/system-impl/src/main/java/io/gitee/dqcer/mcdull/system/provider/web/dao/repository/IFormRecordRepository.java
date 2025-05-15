package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordEntity;

import java.util.List;


/**
 * Form record repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IFormRecordRepository extends IRepository<FormRecordEntity> {

    /**
     * select by form id
     *
     * @param formId formId
     * @return List<FormRecordEntity>
     */
    List<FormRecordEntity> selectByFormId(Integer formId);
}