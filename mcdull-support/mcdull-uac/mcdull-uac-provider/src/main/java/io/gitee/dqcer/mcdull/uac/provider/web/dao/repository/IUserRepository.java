package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;

import java.time.LocalTime;
import java.util.Date;

/**
 * 用户 数据库操作封装接口层
 *
 * @author dqcer
 * @since 2022/12/26
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
    UserDO get(String account);

    /**
     * update
     *
     * @param id       身份证件
     * @param inactive 不活跃
     * @return boolean
     */
    boolean update(Long id, boolean inactive);

    boolean update(Long id, String password);

    void updateLoginTime(Long userId, Date nowTime);
}
