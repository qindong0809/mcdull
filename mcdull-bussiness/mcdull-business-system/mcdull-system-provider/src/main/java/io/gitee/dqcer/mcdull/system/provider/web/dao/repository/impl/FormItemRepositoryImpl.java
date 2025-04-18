package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormItemEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FormItemMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFormItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Form item repository
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormItemRepositoryImpl extends
        CrudRepository<FormItemMapper, FormItemEntity> implements IFormItemRepository {

    @Override
    public void deleteByFormId(Integer formId) {
        LambdaQueryWrapper<FormItemEntity> query = Wrappers.lambdaQuery();
        query.eq(FormItemEntity::getFormId, formId);
        this.remove(query);
    }

    @Override
    public List<FormItemEntity> selectByFormId(Integer formId) {
        LambdaQueryWrapper<FormItemEntity> query = Wrappers.lambdaQuery();
        query.eq(FormItemEntity::getFormId, formId);
        return this.list(query);
    }
}
