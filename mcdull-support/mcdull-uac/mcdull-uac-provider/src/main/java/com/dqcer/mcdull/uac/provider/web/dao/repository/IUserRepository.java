package com.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.entity.SysUserEntity;

public interface IUserRepository extends IService<SysUserEntity> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return {@link Page}<{@link SysUserEntity}>
     */
    Page<SysUserEntity> selectPage(UserLiteDTO dto);

    /**
     * 插入
     *
     * @param entity 实体
     * @return {@link Long}
     */
    Long insert(SysUserEntity entity);
}