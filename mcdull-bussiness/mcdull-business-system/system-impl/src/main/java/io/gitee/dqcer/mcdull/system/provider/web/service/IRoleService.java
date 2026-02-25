package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.dto.ReasonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * Role Service
 *
 * @author dqcer
 * @since 2024/7/25 9:27
 */

public interface IRoleService extends IRepository<RoleEntity> {

    /**
     * detail
     *
     * @param id id
     * @return {@link RoleVO }
     */
    RoleVO detail(Integer id);

    /**
     * insert
     *
     * @param dto DTO
     */
    void insert(RoleAddDTO dto);

    /**
     * delete
     *
     * @param id id
     */
    void delete(Integer id);

    /**
     * map
     *
     * @param userIdList userIdList
     * @return {@link Map }<{@link Integer }, {@link List }<{@link RoleEntity }>>
     */
    Map<Integer, List<RoleEntity>> getRoleMap(List<Integer> userIdList);

    /**
     * update
     *
     * @param id  id
     * @param dto DTO
     * @return boolean
     */
    boolean update(Integer id, RoleUpdateDTO dto);

    /**
     * delete
     *
     * @param id  id
     * @param dto DTO
     * @return boolean
     */
    boolean delete(Integer id, ReasonDTO dto);

    /**
     * insert
     *
     * @param id  id
     * @param dto DTO
     * @return boolean
     */
    boolean insertPermission(Integer id, RolePermissionInsertDTO dto);

    /**
     * all
     *
     * @return {@link List }<{@link RoleVO }>
     */
    List<RoleVO> all();

    /**
     * get
     *
     * @param roleId roleId
     * @return {@link RoleVO }
     */
    RoleVO get(Integer roleId);

    /**
     * update role
     *
     * @param dto DTO
     */
    void updateRole(RoleUpdateDTO dto);

    /**
     * update role menu
     *
     * @param dto DTO
     */
    void updateRoleMenu(RoleMenuUpdateDTO dto);

    /**
     * batch remove role employee
     *
     * @param dto DTO
     */
    void batchRemoveRoleEmployee(RoleEmployeeUpdateDTO dto);

    /**
     * map
     *
     * @param roleIdList roleIdList
     * @return {@link Map }<{@link Integer }, {@link String }>
     */
    Map<Integer, String> mapName(List<Integer> roleIdList);
}
