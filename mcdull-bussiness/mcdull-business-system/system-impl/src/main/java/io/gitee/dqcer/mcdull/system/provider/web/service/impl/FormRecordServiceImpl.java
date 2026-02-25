package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicCurdServiceImpl;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.FormRecordMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFormRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormRecordServiceImpl extends BasicCurdServiceImpl<FormRecordMapper, FormRecordEntity> implements IFormRecordService {

    @Override
    public List<FormRecordEntity> selectByFormId(Integer formId) {
        LambdaQueryWrapper<FormRecordEntity> query = Wrappers.lambdaQuery();
        query.eq(FormRecordEntity::getFormId, formId);
        return this.list(query);
    }
}
