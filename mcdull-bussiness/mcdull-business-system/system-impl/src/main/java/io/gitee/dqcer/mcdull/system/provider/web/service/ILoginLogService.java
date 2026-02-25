package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginLogQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLogEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLogVO;

/**
 *  Login Log Service
 *
 * @author dqcer
 * @since 2024/7/25 9:24
 */

public interface ILoginLogService extends IRepository<LoginLogEntity> {

    /**
     * query by page
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link LoginLogVO }>
     */
    PagedVO<LoginLogVO> queryByPage(LoginLogQueryDTO dto);

    /**
     * add
     *
     * @param entity entity
     */
    void add(LoginLogEntity entity);

    /**
     * get first login log
     *
     * @param loginName loginName
     * @return {@link LoginLogEntity }
     */
    LoginLogEntity getFirstLoginLog(String loginName);

    /**
     * get last login log
     *
     * @param loginName loginName
     * @return {@link LoginLogEntity }
     */
    LoginLogEntity getLastLoginLog(String loginName);

    /**
     * export data
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(LoginLogQueryDTO dto);
}
