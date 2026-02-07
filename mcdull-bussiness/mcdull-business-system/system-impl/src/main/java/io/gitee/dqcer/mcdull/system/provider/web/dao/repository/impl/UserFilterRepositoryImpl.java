package io.gitee.dqcer.mcdull.system.provider.web.dao.repository.impl;


import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserFilterEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.UserFilterMapper;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IUserFilterRepository;
import org.springframework.stereotype.Service;


/**
 * 用户过滤存储库 impl
 *
 * @author dqcer
 * @since 2026/02/06
 */
@Service
public class UserFilterRepositoryImpl extends
        CrudRepository<UserFilterMapper, UserFilterEntity> implements IUserFilterRepository {

}
