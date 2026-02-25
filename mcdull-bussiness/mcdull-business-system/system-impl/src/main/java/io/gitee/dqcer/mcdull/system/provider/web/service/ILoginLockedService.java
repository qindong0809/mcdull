package io.gitee.dqcer.mcdull.system.provider.web.service;

import com.baomidou.mybatisplus.extension.repository.IRepository;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.LoginLockedEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LoginLockedVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Login Locked Service
 *
 * @author dqcer
 * @since 2026/02/25
 */

public interface ILoginLockedService extends IRepository<LoginLockedEntity> {

    /**
     * get
     *
     * @param loginName loginName
     * @return {@link LoginLockedEntity }
     */
    LoginLockedEntity get(String loginName);

    /**
     * query page
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link LoginLockedVO }>
     */
    PagedVO<LoginLockedVO> queryPage(LoginFailQueryDTO dto);

    /**
     * un lock
     *
     * @param idList idList
     */
    void unlock(List<Integer> idList);

    /**
     * update fail count
     *
     * @param username               username
     * @param failedLoginMaximumTime failedLoginMaximumTime
     * @param failCount              failCount
     */
    void updateFailCount(String username, Integer failedLoginMaximumTime, int failCount);

    /**
     * inactive
     *
     * @param username username
     */
    void inactive(String username);

    /**
     * get login failure count
     *
     * @param loginName loginName
     * @return int
     */
    int getLoginFailureCount(String loginName);
}
