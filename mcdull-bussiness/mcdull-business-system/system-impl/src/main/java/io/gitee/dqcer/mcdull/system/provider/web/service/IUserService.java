package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.vo.LabelValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * User Service
 *
 * @author dqcer
 * @since 2024/7/25 9:30
 */

public interface IUserService extends IRepository<UserEntity> {

    /**
     * 密码检查
     *
     * @param entity        实体
     * @param passwordParam password param
     * @return boolean
     */
    boolean passwordCheck(UserEntity entity, String passwordParam);

    /**
     * 按页面列表
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link UserVO }>
     */
    PagedVO<UserVO> listByPage(UserListDTO dto);

    /**
     * 插入
     *
     * @param dto DTO
     * @return {@link Integer }
     */
    Integer insert(UserAddDTO dto);

    /**
     * 去
     *
     * @param username 用户名
     * @return {@link UserEntity }
     */
    UserEntity get(String username);

    /**
     *
     *
     * @param id 身份证
     */
    void toggleActive(Integer id);

    /**
     * 删除
     *
     * @param id 身份证
     * @return boolean
     */
    boolean delete(List<Integer> id);

    /**
     * 更新密码
     *
     * @param id  身份证
     * @param dto DTO
     * @return {@link Integer }
     */
    Integer updatePassword(Integer id, UserUpdatePasswordDTO dto);

    /**
     * 更新
     *
     * @param id  身份证
     * @param dto DTO
     * @return {@link Integer }
     */
    Integer update(Integer id, UserUpdateDTO dto);

    /**
     * 获取资源模块列表
     *
     * @param userId 用户ID
     * @return {@link List }<{@link UserPowerVO }>
     */
    List<UserPowerVO> getResourceModuleList(Integer userId);

    /**
     * userid、entity
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link UserEntity}>
     */
    Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList);

    /**
     * 是否管理员
     *
     * @param userId 用户ID
     * @return boolean
     */
    boolean isAdmin(Integer userId);

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

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @return {@link String }
     */
    String resetPassword(Integer userId);

    /**
     * 重置密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return {@link String }
     */
    String resetPassword(Integer userId, String newPassword);

    /**
     * 查询全部
     *
     * @param disabledFlag 残疾旗帜
     * @return {@link List }<{@link UserAllVO }>
     */
    List<UserAllVO> queryAll(Boolean disabledFlag);

    /**
     * 批处理更新部门
     *
     * @param dto DTO
     */
    void batchUpdateDepartment(UserBatchUpdateDepartmentDTO dto);

    /**
     * 查询
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link UserVO }>
     */
    PagedVO<UserVO> query(RoleUserQueryDTO dto);

    /**
     * 按角色获取所有 ID
     *
     * @param roleId 角色ID
     * @return {@link List }<{@link UserVO }>
     */
    List<UserVO> getAllByRoleId(Integer roleId);

    /**
     * 按角色添加用户列表
     *
     * @param dto DTO
     */
    void addUserListByRole(RoleUserUpdateDTO dto);

    /**
     * 按部门列表
     *
     * @param deptIdList deptIdList
     * @return {@link List }<{@link UserEntity }>
     */
    List<UserEntity> listByDeptList(List<Integer> deptIdList);

    /**
     * 喜欢
     *
     * @param userName 用户名
     * @return {@link List }<{@link UserEntity }>
     */
    List<UserEntity> getLike(String userName);

    /**
     * 更新登录时间
     *
     * @param id 身份证
     */
    void updateLoginTime(Integer id);

    /**
     * 按角色ID分类页面
     *
     * @param roleId 角色ID
     * @param dto    DTO
     * @return {@link PagedVO }<{@link UserVO }>
     */
    PagedVO<UserVO> pageByRoleId(Integer roleId, UserListDTO dto);

    /**
     * 获取真实姓名
     *
     * @param userId 用户ID
     * @return {@link String }
     */
    String getActualName(Integer userId);

    /**
     * 出口数据
     *
     */
    void exportData();

    /**
     * 下载模板
     *
     */
    void downloadTemplate();

    /**
     * 导入数据
     *
     * @param file 文件
     * @return boolean
     */
    boolean importData(MultipartFile file);

    /**
     * like
     *
     * @param userName userName
     * @return List
     */
    List<UserEntity> like(String userName);

    /**
     * 按登录名获取名称映射
     *
     * @param loginList 登录列表
     * @return {@link Map }<{@link String }, {@link String }>
     */
    Map<String, String> getNameMapByLoginName(List<String> loginList);


    /**
     * 获取负责人列表
     *
     * @return {@link List }<{@link LabelValueVO }<{@link Integer }, {@link String }>>
     */
    List<LabelValueVO<Integer, String>> getResponsibleList();

    /**
     * 获取用户 ID 列表
     *
     * @param departmentId 部门 ID
     * @return {@link List }<{@link Integer }>
     */
    List<Integer> getUserIdList(Integer departmentId);

    /**
     * 获取地图
     *
     * @param list 列表
     * @return {@link Map }<{@link Integer }, {@link String }>
     */
    Map<Integer, String> getMap(List<? extends BaseEntity<Integer>> list);
}
