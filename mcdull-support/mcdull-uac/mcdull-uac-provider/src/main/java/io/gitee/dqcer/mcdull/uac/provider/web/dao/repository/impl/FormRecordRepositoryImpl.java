package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FormRecordMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormRecordRepositoryImpl extends
        ServiceImpl<FormRecordMapper, FormRecordEntity> implements IFormRecordRepository {

    @Override
    public List<FormRecordEntity> selectByFormId(Integer formId) {
        LambdaQueryWrapper<FormRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(FormRecordEntity::getFormId, formId);
        return this.list(query);
    }
}
