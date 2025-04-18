package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordItemEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FormRecordItemMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFormRecordItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Form record item repository impl
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormRecordItemRepositoryImpl extends
        CrudRepository<FormRecordItemMapper, FormRecordItemEntity> implements IFormRecordItemRepository {

    @Override
    public List<FormRecordItemEntity> selectByFormId(Integer formId) {
        LambdaQueryWrapper<FormRecordItemEntity> query = Wrappers.lambdaQuery();
        query.eq(FormRecordItemEntity::getFormId, formId);
        return this.list(query);
    }

    @Override
    public List<FormRecordItemEntity> selectByRecordId(Integer recordId) {
        LambdaQueryWrapper<FormRecordItemEntity> query = Wrappers.lambdaQuery();
        query.eq(FormRecordItemEntity::getFormRecordId, recordId);
        return this.list(query);
    }
}
