package io.gitee.dqcer.mcdull.admin.web.service.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEmailConfigDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserEmailConfigVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserProfileVO;
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

    Result<PagedVO<UserVO>> paged(UserLiteDTO dto);

    Result<UserDetailVO> detail(Long userId);

    Result<Long> add(UserInsertDTO dto);


    Result<Long> edit(UserEditDTO dto);

    Result<Long> updateStatus(StatusDTO dto);

    Result<Long> delete(Long id);

    Result<Long> resetPassword(UserLiteDTO dto);

    void export(UserLiteDTO dto);

    Result<UserProfileVO> profile();

    Result<Boolean> updateEmailConfig(UserEmailConfigDTO dto);

    Result<UserEmailConfigVO> detailEmailConfig();

}
