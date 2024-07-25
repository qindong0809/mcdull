package io.gitee.dqcer.mcdull.uac.provider.web.dao.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.LoginLockedEntity;

/**
 * Login Locked Repository
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ILoginLockedRepository extends IService<LoginLockedEntity>  {

    /**
     * get
     *
     * @param loginName loginName
     * @return {@link LoginLockedEntity}
     */
    LoginLockedEntity get(String loginName);

    /**
     * select page
     *
     * @param dto dto
     * @return {@link Page}<{@link LoginLockedEntity}>
     */
    Page<LoginLockedEntity> selectPage(LoginFailQueryDTO dto);
}