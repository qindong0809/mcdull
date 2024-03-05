package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
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
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.framework.web.service.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdatePasswordDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleDO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserDO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
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
public class UserServiceImpl extends BasicServiceImpl<IUserRepository>  implements IUserService {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;



    @Override
    public boolean passwordCheck(UserDO entity, String passwordParam) {
        if (ObjUtil.isNotNull(entity) && StrUtil.isNotBlank(passwordParam)) {
            String password = entity.getPassword();
            return password.equals(Sha1Util.getSha1(passwordParam + entity.getSalt()));
        }
        return false;
    }

    public String buildPassword(String param, String salt) {
        return Sha1Util.getSha1(param + salt);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedVO<UserVO> listByPage(UserLiteDTO dto) {
        Page<UserDO> entityPage = baseRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        List<UserDO> userList = entityPage.getRecords();
        if (entityPage.getTotal() == GlobalConstant.Number.NUMBER_0) {
            return PageUtil.toPage(voList, entityPage);
        }
        Set<Long> createdBySet = userList.stream().map(BaseDO::getCreatedBy).collect(Collectors.toSet());
        Set<Long> updatedBySet = userList.stream().map(BaseDO::getUpdatedBy)
                .filter(ObjUtil::isNotNull).collect(Collectors.toSet());
        createdBySet.addAll(updatedBySet);
        List<UserDO> list = baseRepository.listByIds(createdBySet);
        Map<Long, UserDO> userMap = new HashMap<>(list.size());
        if (CollUtil.isNotEmpty(list)) {
            userMap = list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
        }

        List<Long> userIdList = userList.stream().map(IdDO::getId).collect(Collectors.toList());
        Map<Long, List<RoleDO>> roleListMap = roleService.getRoleMap(userIdList);

        for (UserDO entity : userList) {
            UserVO vo = UserConvert.entityToVO(entity);
            this.setUserFieldValue(userMap, vo);
            this.setRoleListFieldValue(roleListMap, vo);
            voList.add(vo);
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private void setRoleListFieldValue(Map<Long, List<RoleDO>> roleListMap, UserVO vo) {
        Long id = vo.getUserId();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insert(UserInsertDTO dto) {
        this.checkParam(dto);
        Long id = this.buildEntityAndInsert(dto);
        userRoleService.deleteAndInsert(id, dto.getRoleIds());
        return id;
    }

    @Override
    public UserDO get(String username) {
        if (StrUtil.isNotBlank(username)) {
            return baseRepository.get(username);
        }
        return null;
    }

    private void checkParam(UserInsertDTO dto) {
        UserDO user = baseRepository.get(dto.getAccount());
        if (ObjUtil.isNotNull(user)) {
            throw new BusinessException(I18nConstants.DATA_EXISTS);
        }
    }

    private Long buildEntityAndInsert(UserInsertDTO dto) {
        UserDO entity = UserConvert.insertDtoToEntity(dto);
        String salt = RandomUtil.uuid();
        String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
        entity.setSalt(salt);
        entity.setPassword(password);
        entity.setType(1);
        return baseRepository.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long toggleActive(Long id) {
        UserDO dbData = baseRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        boolean success = baseRepository.update(id, !dbData.getInactive());
        if (!success) {
            log.error("数据更新失败，id:{}", id);
            throw new BusinessException(I18nConstants.DB_OPERATION_FAILED);
        }
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        UserDO dbData = baseRepository.getById(id);
        if (null == dbData) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        UserDO entity = new UserDO();
        entity.setId(id);
        boolean success = baseRepository.removeById(id);
        if (!success) {
            log.error("数据删除失败，entity:{}", entity);
            throw new BusinessException(I18nConstants.DB_OPERATION_FAILED);
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updatePassword(Long id, UserUpdatePasswordDTO dto) {
        UserDO entity = baseRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        boolean isOk = this.passwordCheck(entity, dto.getOldPassword());
        if (!isOk) {
            throw new BusinessException("user.password.incorrect");
        }
        String password = this.buildPassword(dto.getNewPassword(), entity.getSalt());
        baseRepository.update(id, password);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long update(Long id, UserUpdateDTO dto) {
        this.checkParamThrowException(id, dto);
        UserDO updateDO = UserConvert.updateDtoToEntity(dto);
        updateDO.setId(id);
        baseRepository.updateById(updateDO);
        userRoleService.deleteAndInsert(id, dto.getRoleIds());
        return updateDO.getId();
    }

    private void checkParamThrowException(Long id, UserUpdateDTO dto) {
        UserDO entity = baseRepository.getById(id);
        if (entity == null) {
            log.warn("数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        UserDO userDO = baseRepository.get(dto.getAccount());
        if (userDO != null) {
            if (!userDO.getId().equals(id)) {
                log.warn("账号名称已存在 account: {}", dto.getAccount());
                throw new BusinessException(I18nConstants.DATA_EXISTS);
            }
        }
    }

    @Override
    public List<UserPowerVO> getResourceModuleList(Long userId) {
        Map<Long, List<RoleDO>> roleListMap = roleService.getRoleMap(ListUtil.of(userId));
        List<RoleDO> roleDOList = roleListMap.get(userId);
        if (CollUtil.isEmpty(roleDOList)) {
            log.warn("userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        List<UserPowerVO> vos = roleDOList.stream().map(i-> {
            UserPowerVO vo = new UserPowerVO();
            vo.setRoleId(i.getId());
            vo.setCode(i.getCode());
            return vo;
        }).collect(Collectors.toList());

        Set<Long> roleSet = vos.stream().map(UserPowerVO::getRoleId).collect(Collectors.toSet());
        Map<Long, List<String>> keyRoleIdValueMenuCode = menuService.getMenuCodeListMap(new ArrayList<>(roleSet));
        for (UserPowerVO vo : vos) {
            String code = vo.getCode();
            if (ObjectUtil.equals(GlobalConstant.SUPER_ADMIN_ROLE, code)) {
                List<String> allCodeList = menuService.getAllCodeList();
                vo.setModules(allCodeList);
                continue;
            }
            List<String> menuCodeList = keyRoleIdValueMenuCode.get(vo.getRoleId());
            if (CollUtil.isEmpty(menuCodeList)) {
                log.warn("userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                menuCodeList = Collections.emptyList();
            }
            vo.setModules(menuCodeList);
        }
        return vos;
    }

    @Override
    public Map<Long, UserDO> getEntityMap(List<Long> userIdList) {
        List<UserDO> list = this.list(userIdList);
        return list.stream().collect(Collectors.toMap(IdDO::getId, Function.identity()));
    }

    @Override
    public Map<Long, String> getNameMap(List<Long> userIdList) {
        List<UserDO> list = this.list(userIdList);
        return list.stream().collect(Collectors.toMap(IdDO::getId, UserDO::getNickName));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLoginTime(Long userId, Date nowTime) {
        if (ObjUtil.isNull(userId) || ObjUtil.isNull(nowTime)) {
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        baseRepository.updateLoginTime(userId, nowTime);
    }

    @Override
    public UserVO get(Long userId) {
        if (ObjUtil.isNotNull(userId)) {
            List<UserDO> list = this.list(ListUtil.of(userId));
            if (CollUtil.isNotEmpty(list)) {
                UserDO user = list.get(0);
                return UserConvert.entityToVO(user);
            }
        }
        return null;
    }

    private List<UserDO> list(List<Long> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            List<UserDO> userList = baseRepository.listByIds(userIdList);
            return CollUtil.emptyIfNull(userList);
        }
        return Collections.emptyList();
    }


}
