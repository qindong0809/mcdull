package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleDO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuDO;

import java.util.List;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleRepository extends IService<RoleDO> {


    List<RoleDO> queryListByIds(List<Long> ids);


    Page<RoleDO> selectPage(RoleLiteDTO dto);


    Long insert(RoleDO entity);


    boolean exist(RoleDO roleDO);


    void deleteBatchByIds(List<Long> ids);


    List<RoleMenuDO> getMenuByRole(List<Long> roles);

    List<RoleDO> getAll();

    void batchSaveMenu(Long roleId, List<Long> menuIds);

    void batchUpdateMenu(Long roleId, List<Long> menuIds);
}
