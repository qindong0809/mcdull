package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.UserDO;

import java.util.List;

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

}
