package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdatePasswordDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserService {
    boolean passwordCheck(UserDO entity, String passwordParam);

    PagedVO<UserVO> listByPage(UserLiteDTO dto);

    Long insert(UserInsertDTO dto);

    Long toggleActive(Long id);

    boolean delete(Long id);

    Long updatePassword(Long id, UserUpdatePasswordDTO dto);

    Long update(Long id, UserUpdateDTO dto);

    List<UserPowerVO> getResourceModuleList(Long userId);

    Map<Long, UserDO> getEntityMap(List<Long> userIdList);

    Map<Long, String> getNameMap(List<Long> userIdList);
}
