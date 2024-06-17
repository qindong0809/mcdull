package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FormRecordItemEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.FormRecordItemMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFormRecordItemRepository;
import org.springframework.stereotype.Service;


/**
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@Service
public class FormRecordItemRepositoryImpl extends
        ServiceImpl<FormRecordItemMapper, FormRecordItemEntity> implements IFormRecordItemRepository {

}
