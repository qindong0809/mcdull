package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.system.provider.model.vo.ApplicationVO;

import java.util.List;

/**
 * 应用 Service
 *
 * @author dqcer
 * @since 2026/11/23
 */
public interface IApplicationService {

    /**
     * 查询当前用户权限内可见的应用
     *
     * @return 应用列表
     */
    List<ApplicationVO> listVisible();
}
