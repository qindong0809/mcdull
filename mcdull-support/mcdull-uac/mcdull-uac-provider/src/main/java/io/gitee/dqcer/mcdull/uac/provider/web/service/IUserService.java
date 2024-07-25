package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * User Service
 *
 * @author dqcer
 * @since 2024/7/25 9:30
 */

public interface IUserService {
    boolean passwordCheck(UserEntity entity, String passwordParam);

    PagedVO<UserVO> listByPage(UserListDTO dto);

    Integer insert(UserAddDTO dto);

    UserEntity get(String username);

    void toggleActive(Integer id);

    boolean delete(List<Integer> id);

    Integer updatePassword(Integer id, UserUpdatePasswordDTO dto);

    Integer update(Integer id, UserUpdateDTO dto);

    List<UserPowerVO> getResourceModuleList(Integer userId);

    /**
     * userid、entity
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link UserEntity}>
     */
    Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList);

    /**
     * userid、username
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    Map<Integer, String> getNameMap(List<Integer> userIdList);

    /**
     * get
     *
     * @param userId 用户id
     * @return {@link UserVO}
     */
    UserEntity get(Integer userId);

    String resetPassword(Integer userId);

    String resetPassword(Integer userId, String newPassword);

    List<UserAllVO> queryAll(Boolean disabledFlag);

    void batchUpdateDepartment(UserBatchUpdateDepartmentDTO dto);

    PagedVO<UserVO> query(RoleUserQueryDTO dto);

    List<UserVO> getAllByRoleId(Integer roleId);

    void addUserListByRole(RoleUserUpdateDTO dto);

    List<UserEntity> listByDeptList(List<Integer> deptIdList);

    List<UserEntity> getLike(String userName);

    void updateLoginTime(Integer id);

    PagedVO<UserVO> pageByRoleId(Integer roleId, UserListDTO dto);

    String getActualName(Integer userId);
}
