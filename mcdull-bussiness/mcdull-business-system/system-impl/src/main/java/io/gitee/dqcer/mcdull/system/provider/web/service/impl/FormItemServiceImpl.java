package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormItemEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.FormItemMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Form item service impl
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormItemServiceImpl extends BasicCurdServiceImpl<FormItemMapper, FormItemEntity> implements IFormItemService {

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
