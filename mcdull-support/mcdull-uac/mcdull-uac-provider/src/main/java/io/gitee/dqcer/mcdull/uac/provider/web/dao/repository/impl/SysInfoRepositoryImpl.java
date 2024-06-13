package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.SysInfoMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISysInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
* @author dqcer
* @since 2024-04-29
*/
@Service
public class SysInfoRepositoryImpl
        extends ServiceImpl<SysInfoMapper, SysInfoEntity>  implements ISysInfoRepository {

    private static final Logger log = LoggerFactory.getLogger(SysInfoRepositoryImpl.class);

}