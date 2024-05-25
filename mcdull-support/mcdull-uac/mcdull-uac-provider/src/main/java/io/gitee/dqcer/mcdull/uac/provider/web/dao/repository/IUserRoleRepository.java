package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleUserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户角色 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserRoleRepository extends IService<RoleUserEntity> {

    /**
     * 更新根据用户id
     *
     * @param id      id
     * @param roleIds 角色id
     */
    void insert(Integer id, List<Integer> roleIds);

    /**
     * 角色map
     *
     * @param userCollection 用户集合
     * @return {@link Map}<{@link Long}, {@link List}<{@link Long}>>
     */
    Map<Integer, List<Integer>> roleIdListMap(Collection<Integer> userCollection);

    /**
     * 列表
     *
     * @param userIdList 用户id列表
     * @return {@link List}<{@link RoleUserEntity}>
     */
    List<RoleUserEntity> list(List<Integer> userIdList);

    List<Integer> listByRole(Integer roleId);

    void insert(List<Integer> userIdList, Integer roleId);

    List<RoleUserEntity> list(List<Integer> userIdList, Integer roleId);
}
