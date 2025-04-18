package io.gitee.dqcer.mcdull.system.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.system.provider.model.dto.RoleUserQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;

import java.util.List;

/**
 * User Repository
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserRepository extends IRepository<UserEntity> {

    /**
     * 分页查询
     *
     * @param dto        dto
     * @param deptIdList deptIdList
     * @return {@link Page}<{@link UserEntity}>
     */
    Page<UserEntity> selectPage(UserListDTO dto, List<Integer> deptIdList, List<Integer> notContainsUserIdList);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Integer insert(UserEntity entity);

    /**
     * 单个根据账户名称
     *
     * @param account 账户
     * @return {@link UserEntity}
     */
    UserEntity get(String account);

    /**
     * update
     *
     * @param id       身份证件
     * @param inactive 不活跃
     * @return boolean
     */
    boolean update(Integer id, boolean inactive);

    /**
     * 更新密码
     *
     * @param id       身份证件
     * @param password 密码
     * @return boolean
     */
    boolean update(Integer id, String password);

    /**
     * 根据角色id分页
     *
     * @param userIdList 用户id列表
     * @param dto        dto
     * @return {@link Page}<{@link UserEntity}>
     */
    Page<UserEntity> selectPageByRoleId(List<Integer> userIdList, RoleUserQueryDTO dto);

    /**
     * 根据部门id列表
     *
     * @param deptIdList 部门id列表
     * @return {@link List}<{@link UserEntity}>
     */
    List<UserEntity> listByDeptList(List<Integer> deptIdList);

    /**
     * 模糊查询
     *
     * @param userName 用户名
     * @return {@link List}<{@link UserEntity}>
     */
    List<UserEntity> like(String userName);
}
