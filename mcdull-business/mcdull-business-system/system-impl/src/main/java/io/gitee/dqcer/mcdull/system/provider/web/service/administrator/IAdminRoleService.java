package io.gitee.dqcer.mcdull.system.provider.web.service.administrator;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.RoleSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminRoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.AdminRoleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.administrator.RolePermissionVO;

import java.util.List;

public interface IAdminRoleService extends IService<AdminRoleEntity> {


    List<AdminRoleEntity> getList();

    List<AdminRoleVO> getRoleList();

    List<RolePermissionVO> getRoleTreeList();

    AdminRoleVO getRole(Integer id);

    Integer saveRole(RoleSaveDTO dto);

    Boolean editRole(Integer id, RoleSaveDTO dto);

    Boolean deleteRole(List<Integer> ids);

    Boolean editRoleMenu(Integer id, List<Integer> menuIds);

    void saveUserList(Integer id, List<Integer> userId);

    Boolean deleteRoleUserRel(Integer id, List<Integer> userIdList);
}
