package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormItemEntity;

import java.util.List;


/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
public interface IFormItemRepository extends IService<FormItemEntity> {


    void deleteByFormId(Integer formId);

    List<FormItemEntity> selectByFormId(Integer formId);
}