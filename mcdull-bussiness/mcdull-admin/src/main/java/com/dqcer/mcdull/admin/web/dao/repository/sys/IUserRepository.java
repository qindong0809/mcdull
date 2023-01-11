package com.dqcer.mcdull.admin.web.dao.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import com.dqcer.mcdull.admin.model.entity.sys.UserDO;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;

import java.util.List;

/**
 * 用户 数据库操作封装接口层
 *
 * @author dqcer
 * @version 2022/12/26
 */
public interface IUserRepository extends IService<UserDO> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link UserDO}>
     */
    Page<UserDO> selectPage(UserLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(UserDO entity);

    /**
     * 单个根据账户名称
     *
     * @param account 账户
     * @return {@link UserDO}
     */
    UserDO oneByAccount(String account);

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserPowerVO}>
     */
    List<UserPowerVO> queryResourceModules(Long userId);

    /**
     * 更新登录时间通过id
     *
     * @param userId 用户id
     */
    void updateLoginTimeById(Long userId);

    /**
     * 查询用户帐户
     *
     * @param account 账户
     * @return {@link UserDO}
     */
    UserDO queryUserByAccount(String account);
}
