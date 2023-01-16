package io.gitee.dqcer.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.uac.provider.model.entity.UserDO;

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
