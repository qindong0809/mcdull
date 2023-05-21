package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;

/**
 * user 服务
 *
 * @author dqcer
 * @since 2023/01/15 13:01:86
 */
public interface IUserService {


    /**
     * 列表页
     *
     * @param dto dto
     * @return {@link Result<PagedVO< UserVO >>}
     */
    Result<PagedVO<UserVO>> listByPage(UserLiteDTO dto);


    /**
     * 单个详情
     *
     * @param userId userId
     * @return {@link Result<UserVO>}
     */
    Result<UserDetailVO> detail(Long userId);


    /**
     * 插入
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    Result<Long> insert(UserLiteDTO dto);

    /**
     * 更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    Result<Long> update(UserLiteDTO dto);

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    Result<Long> updateStatus(StatusDTO dto);

    /**
     * 删除
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    Result<Long> delete(UserLiteDTO dto);

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    Result<Long> resetPassword(UserLiteDTO dto);


    Result<Boolean> export(UserLiteDTO dto);
}
