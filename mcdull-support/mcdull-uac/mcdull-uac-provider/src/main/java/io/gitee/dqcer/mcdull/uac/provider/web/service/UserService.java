package io.gitee.dqcer.mcdull.uac.provider.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseDO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdDO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdatePasswordDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户服务
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private IUserRepository userRepository;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;


    @Transactional(readOnly = true)
    public PagedVO<UserVO> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = userRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        List<UserDO> userList = entityPage.getRecords();
        if (entityPage.getTotal() == 0) {
            return PageUtil.toPage(voList, entityPage);
        }
        Set<Long> createdBySet = userList.stream().map(BaseDO::getCreatedBy).collect(Collectors.toSet());
        Set<Long> updatedBySet = userList.stream().map(BaseDO::getUpdatedBy)
                .filter(ObjUtil::isNotNull).collect(Collectors.toSet());
        createdBySet.addAll(updatedBySet);
        List<UserDO> list = userRepository.listByIds(createdBySet);
        Map<Long, UserDO> userMap = new HashMap<>(list.size());
        if (CollUtil.isNotEmpty(list)) {
            userMap = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        }

        List<Long> userIdList = userList.stream().map(IdDO::getId).collect(Collectors.toList());
        Map<Long, List<RoleDO>> roleListMap = roleService.getRoleMap(userIdList);

        for (UserDO entity : userList) {
            UserVO vo = UserConvert.entity2VO(entity);
            this.setUserFieldValue(userMap, vo);
            this.setRoleListFieldValue(roleListMap, vo);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private void setRoleListFieldValue(Map<Long, List<RoleDO>> roleListMap, UserVO vo) {
        Long id = vo.getId();
        List<RoleDO> list = roleListMap.getOrDefault(id, ListUtil.empty());
        if (CollUtil.isNotEmpty(list)) {
            List<BaseVO<Long, String>> roleList = list.stream()
                    .map(i -> new BaseVO<>(i.getId(), i.getName())).collect(Collectors.toList());
            vo.setRoles(roleList);
        }
    }

    private void setUserFieldValue(Map<Long, UserDO> userMap, UserVO vo) {
        Long createdBy = vo.getCreatedBy();
        if (ObjUtil.isNotNull(createdBy)) {
            vo.setCreatedByStr(userMap.getOrDefault(createdBy, new UserDO()).getUsername());
        }
        Long updatedBy = vo.getUpdatedBy();
        if (ObjUtil.isNotNull(updatedBy)) {
            vo.setUpdatedByStr(userMap.getOrDefault(updatedBy, new UserDO()).getUsername());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long insert(UserInsertDTO dto) {
        UserDO user = userRepository.get(dto.getAccount());
        if (ObjUtil.isNotNull(user)) {
            throw new BusinessException(I18nConstants.DATA_EXISTS);
        }
        Long id = this.buildEntityAndInsert(dto);
        userRoleService.deleteAndInsert(id, dto.getRoleIds());
        return id;
    }

    private Long buildEntityAndInsert(UserInsertDTO dto) {
        UserDO entity = UserConvert.insertDtoToEntity(dto);
        String salt = RandomUtil.uuid();
        String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
        entity.setSalt(salt);
        entity.setPassword(password);
        entity.setType(1);
        return userRepository.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long toggleActive(Long id) {
        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        boolean success = userRepository.update(id, !dbData.getInactive());
        if (!success) {
            log.error("数据更新失败，id:{}", id);
            throw new BusinessException(I18nConstants.DB_OPERATION_FAILED);
        }
        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        UserDO dbData = userRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        UserDO entity = new UserDO();
        entity.setId(id);
        boolean success = userRepository.removeById(id);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(I18nConstants.DB_OPERATION_FAILED);
        }
        return true;
    }
    

    @Transactional(rollbackFor = Exception.class)
    public Long updatePassword(Long id, UserUpdatePasswordDTO dto) {
        UserDO entity = userRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        // TODO: 2024/2/5
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(Long id, UserUpdateDTO dto) {
        this.checkParamThrowException(id, dto);
        UserDO updateDO = UserConvert.updateDtoToEntity(dto);
        updateDO.setId(id);
        userRepository.updateById(updateDO);
        userRoleService.deleteAndInsert(id, dto.getRoleIds());
        return updateDO.getId();
    }

    private void checkParamThrowException(Long id, UserUpdateDTO dto) {
        UserDO entity = userRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        UserDO userDO = userRepository.get(dto.getAccount());
        if (userDO != null) {
            if (!userDO.getId().equals(id)) {
                log.warn("账号名称已存在 account: {}", dto.getAccount());
                throw new BusinessException(I18nConstants.DATA_EXISTS);
            }
        }
    }

    /**
     * 查询资源模块
     *
     * @param userId 用户id
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    public Result<List<UserPowerVO>> queryResourceModules(Long userId) {
        return Result.success(userRepository.queryResourceModules(userId));
    }



}
