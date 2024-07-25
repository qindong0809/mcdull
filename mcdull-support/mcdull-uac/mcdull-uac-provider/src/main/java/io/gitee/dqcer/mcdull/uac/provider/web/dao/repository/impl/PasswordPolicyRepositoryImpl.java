package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.PasswordPolicyEntity;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.mapper.PasswordPolicyMapper;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IPasswordPolicyRepository;
import org.springframework.stereotype.Service;

/**
 * Password  policy repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
@Service
public class PasswordPolicyRepositoryImpl
        extends ServiceImpl<PasswordPolicyMapper, PasswordPolicyEntity>  implements IPasswordPolicyRepository {


}