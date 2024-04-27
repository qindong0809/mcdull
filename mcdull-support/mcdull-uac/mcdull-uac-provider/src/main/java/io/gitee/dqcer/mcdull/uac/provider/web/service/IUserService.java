package io.gitee.dqcer.mcdull.uac.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdatePasswordDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dqcer
 */
public interface IUserService {
    boolean passwordCheck(UserEntity entity, String passwordParam);

    PagedVO<UserVO> listByPage(UserLiteDTO dto);

    Long insert(UserInsertDTO dto);

    UserEntity get(String username);

    Long toggleActive(Long id);

    boolean delete(Long id);

    Long updatePassword(Long id, UserUpdatePasswordDTO dto);

    Long update(Long id, UserUpdateDTO dto);

    List<UserPowerVO> getResourceModuleList(Long userId);

    /**
     * userid、entity
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link UserEntity}>
     */
    Map<Long, UserEntity> getEntityMap(List<Long> userIdList);

    /**
     * userid、username
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    Map<Long, String> getNameMap(List<Long> userIdList);

    /**
     * get
     *
     * @param userId 用户id
     * @return {@link UserVO}
     */
    UserEntity get(Long userId);
}
