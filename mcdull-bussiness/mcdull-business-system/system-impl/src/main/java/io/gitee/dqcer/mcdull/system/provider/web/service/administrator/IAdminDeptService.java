package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminDeptEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserMenuEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminDeptTreeVO;

public interface IAdminDeptService extends IService<AdminDeptEntity> {


    AdminDeptTreeVO getDeptTree();
}
