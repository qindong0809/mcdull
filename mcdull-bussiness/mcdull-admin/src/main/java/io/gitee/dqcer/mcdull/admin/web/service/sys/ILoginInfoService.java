package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LoginInfoVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
 * 用户登录信息服务 接口定义
 *
 * @author dqcer
 * @since 2023/01/15 15:01:98
 */
public interface ILoginInfoService {

    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result}<{@link PagedVO}<{@link LoginInfoVO}>>
     */
    Result<PagedVO<LoginInfoVO>> listByPage(LoginInfoLiteDTO dto);

    /**
     * 导出excel
     *
     * @param dto dto
     */
    void exportExcel(LoginInfoLiteDTO dto);
}
