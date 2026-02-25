package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FormRecordEntity;

import java.util.List;

public interface IFormRecordService extends IRepository<FormRecordEntity> {

    /**
     * select by form id
     *
     * @param formId formId
     * @return List<FormRecordEntity>
     */
    List<FormRecordEntity> selectByFormId(Integer formId);

}
