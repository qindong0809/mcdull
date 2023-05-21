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


    /**
     * 根据ID列表批量查询数据
     *
     * @param ids id列表
     * @return {@link List<RoleDO>}
     */
    List<RoleDO> queryListByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleDO}>
     */
    Page<RoleDO> selectPage(RoleLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(RoleDO entity);

    /**
     * 存在
     *
     * @param roleDO 角色
     * @return boolean
     */
    boolean exist(RoleDO roleDO);

    /**
     * 根据id删除批处理
     *
     * @param ids id
     */
    void deleteBatchByIds(List<Long> ids);

    /**
     * 获取菜单
     *
     * @param roleId 角色id
     * @return {@link List}<{@link RoleMenuDO}>
     */
    List<RoleMenuDO> getMenuByRole(List<Long> roles);

    List<RoleDO> getAll();

}
