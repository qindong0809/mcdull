package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormItemEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FormItemMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormItemRepository;
import org.springframework.stereotype.Service;


/**
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormItemRepositoryImpl extends
        ServiceImpl<FormItemMapper, FormItemEntity> implements IFormItemRepository {

    @Override
    public void deleteByFormId(Integer formId) {
        LambdaQueryWrapper<FormItemEntity> query = Wrappers.lambdaQuery();
        query.eq(FormItemEntity::getFormId, formId);
        this.remove(query);
    }
}
