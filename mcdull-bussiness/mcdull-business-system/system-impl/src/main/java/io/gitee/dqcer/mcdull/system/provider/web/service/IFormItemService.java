package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormItemEntity;

import java.util.List;

public interface IFormItemService extends IRepository<FormItemEntity> {
    /**
     * delete by form id
     *
     * @param formId form id
     */
    void deleteByFormId(Integer formId);

    /**
     * select by form id
     *
     * @param formId form id
     * @return {@link List}<{@link FormItemEntity}>
     */
    List<FormItemEntity> selectByFormId(Integer formId);

}
