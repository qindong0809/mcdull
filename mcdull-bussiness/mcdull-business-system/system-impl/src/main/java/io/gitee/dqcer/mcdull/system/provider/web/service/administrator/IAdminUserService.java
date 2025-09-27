package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminMenuVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminUserSimpleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminVO;

import java.util.List;
import java.util.Map;

public interface IAdminUserService extends IService<AdminUserEntity> {

    String auth(LogonDTO dto);

    AdminVO getAdminInfo(Integer userId);

    List<AdminMenuVO> getUserRoute(Integer userId);

    Map<Integer, String> getUserNameMap();

    PagedVO<AdminUserVO> getUserList( Integer pageNum, Integer pageSize, Integer deptId, Integer status, String createTime, String description);

    AdminUserSimpleVO getUser(Integer id);
}
