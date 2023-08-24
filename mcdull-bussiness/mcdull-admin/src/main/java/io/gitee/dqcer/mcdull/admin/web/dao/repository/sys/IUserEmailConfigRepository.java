package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserEmailConfigDO;

/**
* @author dqcer
* @since 2023-01-14
*/
public interface IUserEmailConfigRepository extends IService<UserEmailConfigDO>  {

    UserEmailConfigDO getOneByUserId(Long userId);
}