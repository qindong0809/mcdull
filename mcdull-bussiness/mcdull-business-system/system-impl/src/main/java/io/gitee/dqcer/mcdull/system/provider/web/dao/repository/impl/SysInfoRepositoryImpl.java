package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.SysInfoEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.SysInfoMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.ISysInfoRepository;
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