package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RolePageDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IRoleRepository extends IService<RoleEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link RoleEntity}>
     */
    Page<RoleEntity> selectPage(RolePageDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Integer insert(RoleEntity entity);

    /**
     * 角色列表
     *
     * @param userRoleMap 用户角色映射
     * @return {@link Map}<{@link Integer}, {@link List}<{@link RoleEntity}>>
     */
    Map<Integer, List<RoleEntity>> roleListMap(Map<Integer, List<Integer>> userRoleMap);

    /**
     * 删除
     *
     * @param id      id
     * @param reason  原因
     * @return boolean
     */
    boolean delete(Integer id, String reason);

    /**
     * 启用禁用
     *
     * @param id      id
     * @param inactive 启用禁用
     * @return boolean
     */
    boolean toggleStatus(Integer id, boolean inactive);
}
