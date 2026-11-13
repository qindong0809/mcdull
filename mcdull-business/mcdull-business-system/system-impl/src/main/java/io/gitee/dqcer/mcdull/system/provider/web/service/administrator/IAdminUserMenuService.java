package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminVO;

import java.util.List;
import java.util.Map;

public interface IAdminUserMenuService extends IService<AdminUserMenuEntity> {

    Map<Integer, List<Integer>> getUserMenu();

}
