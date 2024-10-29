package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormItemEntity;

import java.util.List;


/**
 * Form item repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IFormItemRepository extends IRepository<FormItemEntity> {

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