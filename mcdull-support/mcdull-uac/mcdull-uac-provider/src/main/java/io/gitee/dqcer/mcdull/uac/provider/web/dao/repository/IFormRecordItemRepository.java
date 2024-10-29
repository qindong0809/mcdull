package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormRecordItemEntity;

import java.util.List;


/**
 * Form record item repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IFormRecordItemRepository extends IRepository<FormRecordItemEntity> {

    /**
     * select by form id
     *
     * @param formId form id
     * @return {@link List}<{@link FormRecordItemEntity}>
     */
    List<FormRecordItemEntity> selectByFormId(Integer formId);

    /**
     * select by record id
     *
     * @param recordId record id
     * @return {@link List}<{@link FormRecordItemEntity}>
     */
    List<FormRecordItemEntity> selectByRecordId(Integer recordId);
}