package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.SessionQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.SessionVO;

import java.util.List;

/**
 * Session Service
 *
 * @author dqcer
 * @since 2024-04-29
 */
public interface ISessionService {

    /**
     * Query 页面
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link SessionVO }>
     */
    PagedVO<SessionVO> queryPage(SessionQueryDTO dto);

    /**
     * 批量踢出
     *
     * @param loginIdList 登录 ID 列表
     */
    void batchKickout(List<Integer> loginIdList);

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(SessionQueryDTO dto);
}
