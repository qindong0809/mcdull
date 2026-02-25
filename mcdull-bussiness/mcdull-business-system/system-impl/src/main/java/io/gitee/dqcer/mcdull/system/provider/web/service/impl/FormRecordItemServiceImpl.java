package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordItemEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FormRecordItemMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormRecordItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Form record item service impl
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormRecordItemServiceImpl extends BasicCurdServiceImpl<FormRecordItemMapper, FormRecordItemEntity> implements IFormRecordItemService {
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
