package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SerialNumberRecordQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SerialNumberRecordEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLogVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISerialNumberRecordRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ISerialNumberRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
*
* @author dqcer
* @since 2024-04-29
*/
@Service
public class SerialNumberRecordServiceImpl
        extends BasicServiceImpl<ISerialNumberRecordRepository> implements ISerialNumberRecordService {


    @Override
    public PagedVO<SerialNumberRecordEntity> query(SerialNumberRecordQueryDTO dto) {
        Page<SerialNumberRecordEntity> entityPage = baseRepository.selectPage(dto);
        return PageUtil.toPage(entityPage);
    }
}
