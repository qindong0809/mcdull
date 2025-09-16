package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;

import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;

public interface IAdminUserService extends IService<AdminUserEntity> {

    String auth(LogonDTO dto);
}
