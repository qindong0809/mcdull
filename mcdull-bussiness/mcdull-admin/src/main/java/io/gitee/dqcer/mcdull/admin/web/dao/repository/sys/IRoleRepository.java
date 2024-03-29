package io.gitee.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.RoleLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleEntity;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.RoleMenuEntity;

import java.util.List;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleRepository extends IService<RoleEntity> {


    List<RoleEntity> queryListByIds(List<Long> ids);


    Page<RoleEntity> selectPage(RoleLiteDTO dto);


    Long insert(RoleEntity entity);


    boolean exist(RoleEntity roleDO);


    void deleteBatchByIds(List<Long> ids);


    List<RoleMenuEntity> getMenuByRole(List<Long> roles);

    List<RoleEntity> getAll();

    void batchSaveMenu(Long roleId, List<Long> menuIds);

    void batchUpdateMenu(Long roleId, List<Long> menuIds);
}
