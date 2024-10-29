package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.SysInfoMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.ISysInfoRepository;
import org.springframework.stereotype.Service;

/**
 * Sys info repository impl
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class SysInfoRepositoryImpl
        extends CrudRepository<SysInfoMapper, SysInfoEntity> implements ISysInfoRepository {

}