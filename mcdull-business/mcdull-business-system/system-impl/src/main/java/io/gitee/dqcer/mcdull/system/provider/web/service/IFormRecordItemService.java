package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordItemEntity;

import java.util.List;

public interface IFormRecordItemService extends IRepository<FormRecordItemEntity> {
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
