package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.RoleUserQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserListDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;

import java.util.List;

/**
 * 用户 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
 */
public interface IUserRepository extends IService<UserEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserEntity}>
     */
    Page<UserEntity> selectPage(UserListDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(UserEntity entity);

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
    boolean update(Long id, boolean inactive);

    boolean update(Long id, String password);

    Page<UserEntity> selectPageByRoleId(List<Long> userIdList, RoleUserQueryDTO dto);

    Page<UserEntity> selectPageByRoleId(List<Long> userIdList, PagedDTO dto);

    List<UserEntity> listByDeptList(List<Long> deptIdList);
}
