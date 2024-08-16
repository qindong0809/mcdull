package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMenuService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserRoleService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User ServiceImpl
 *
 * @author dqcer
 * @since  2022/11/27
 */
@Service
public class UserServiceImpl
        extends BasicServiceImpl<IUserRepository>  implements IUserService {

    @Resource
    private IUserRoleService userRoleService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;

    @Resource
    private IDepartmentRepository departmentRepository;

    private static final String AES_KEY = "1024abcd1024abcd1024abcd1024abcd";

    @Override
    public boolean passwordCheck(UserEntity entity, String passwordParam) {
        if (ObjUtil.isNotNull(entity) && StrUtil.isNotBlank(passwordParam)) {
            String password = entity.getLoginPwd();
            String sha1 = hash(passwordParam);
            return password.equals(sha1);
        }
        return false;
    }

    private static String hash(String webPassword) {
        return Sha1Util.getSha1(decrypt(webPassword));
    }

    public static String decrypt(String data) {
        try {
            // 第一步： Base64 解码
            byte[] base64Decode = Base64.getDecoder().decode(data);

            // 第二步： AES 解密
            AES aes = new AES(AES_KEY.getBytes(StandardCharsets.UTF_8));
            byte[] decryptedBytes = aes.decrypt(base64Decode);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            return StrUtil.EMPTY;
        }
    }

    public String buildPassword(String param) {
        return hash(param);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedVO<UserVO> listByPage(UserListDTO dto) {
        Integer departmentId = dto.getDepartmentId();
        List<Integer> deptIdList = new ArrayList<>();
        if (ObjUtil.isNotNull(departmentId)) {
           List<DepartmentEntity> entityList = departmentRepository.getTreeList(departmentId);
            if (CollUtil.isNotEmpty(entityList)) {
                deptIdList = entityList.stream().map(BaseEntity::getId).collect(Collectors.toList());
            }
            deptIdList.add(departmentId);
        }
        Page<UserEntity> entityPage = baseRepository.selectPage(dto, deptIdList, null);
        List<UserEntity> userList = entityPage.getRecords();
        if (CollUtil.isEmpty(userList)) {
            return PageUtil.empty(dto);
        }
        return PageUtil.toPage(this.getVoList(userList), entityPage);
    }

    private void setRoleListFieldValue(Map<Integer, List<RoleEntity>> roleListMap,
                                       UserVO vo,
                                       Integer userId ) {
        List<RoleEntity> list = roleListMap.getOrDefault(userId, ListUtil.empty());
        if (CollUtil.isNotEmpty(list)) {
            List<Integer> collect = list.stream().map(IdEntity::getId).collect(Collectors.toList());
            List<Integer> roleIdLis = new ArrayList<>();
            collect.forEach(r -> roleIdLis.add(Convert.toInt(r)));
            vo.setRoleIdList(roleIdLis);
            vo.setRoleNameList(list.stream().map(RoleEntity::getRoleName).collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insert(UserAddDTO dto) {
        this.checkParam(dto);
        Integer id = this.buildEntityAndInsert(dto);
        userRoleService.batchUserListByRoleId(id, dto.getRoleIdList());
        return id;
    }

    @Override
    public UserEntity get(String username) {
        if (StrUtil.isNotBlank(username)) {
            return baseRepository.get(username);
        }
        return null;
    }

    private void checkParam(UserAddDTO dto) {
        List<UserEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            this.validNameExist(null, dto.getLoginName(), list,
                    i -> i.getLoginName().equals(dto.getLoginName()));
        }
    }

    private Integer buildEntityAndInsert(UserAddDTO dto) {
        UserEntity entity = UserConvert.insertDtoToEntity(dto);
        entity.setLoginPwd("a29c57c6894dee6e8251510d58c07078ee3f49bf");
        entity.setAdministratorFlag(false);
        return baseRepository.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleActive(Integer id) {
        UserEntity dbData = baseRepository.getById(id);
        if (ObjUtil.isNull(dbData)) {
            LogHelp.warn(log, "数据不存在 id:{}", id);
            throw new BusinessException(I18nConstants.DATA_NOT_EXIST);
        }
        Boolean administratorFlag = dbData.getAdministratorFlag();
        if (administratorFlag != null && administratorFlag) {
            LogHelp.warn(log, "管理员账号禁止禁用，id:{}", id);
            throw new BusinessException(I18nConstants.PERMISSION_DENIED);
        }

        boolean success = baseRepository.update(id, !dbData.getInactive());
        if (BooleanUtil.isFalse(success)) {
            LogHelp.error(log, "数据更新失败，id:{}", id);
            throw new BusinessException(I18nConstants.DB_OPERATION_FAILED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> idList) {
        List<UserEntity> userEntities = baseRepository.listByIds(idList);
        if (userEntities.size() != idList.size()) {
            this.throwDataNotExistException(idList);
        }
        boolean anyMatch = userEntities.stream().anyMatch(UserEntity::getAdministratorFlag);
        if (anyMatch) {
            LogHelp.warn(log, "管理员账号禁止删除，id:{}", idList);
            throw new BusinessException(I18nConstants.PERMISSION_DENIED);
        }
        baseRepository.removeBatchByIds(idList);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updatePassword(Integer id, UserUpdatePasswordDTO dto) {
        UserEntity entity = baseRepository.getById(id);
        if (entity == null) {
            this.throwDataNotExistException(id);
        }
        boolean isOk = this.passwordCheck(entity, dto.getOldPassword());
        if (!isOk) {
            throw new BusinessException("user.password.incorrect");
        }
        String password = this.buildPassword(dto.getNewPassword());
        baseRepository.update(id, password);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(Integer id, UserUpdateDTO dto) {
        UserEntity entity = baseRepository.getById(id);
        if (ObjUtil.isNull(entity)) {
            this.throwDataNotExistException(id);
        }
        this.checkParamThrowException(id, dto);
        UserEntity updateDO = UserConvert.updateDtoToEntity(dto);
        updateDO.setId(id);
        baseRepository.updateById(updateDO);
        userRoleService.batchUserListByRoleId(id, dto.getRoleIdList());
        return updateDO.getId();
    }

    private void checkParamThrowException(Integer id, UserUpdateDTO dto) {
        UserEntity userDO = baseRepository.get(dto.getLoginName());
        if (userDO != null) {
            boolean administratorFlag = userDO.getAdministratorFlag();
            if (administratorFlag) {
                LogHelp.warn(log, "管理员账号禁止操作，id:{}", id);
                throw new BusinessException(I18nConstants.PERMISSION_DENIED);
            }
            if (!userDO.getId().equals(id)) {
                LogHelp.warn(log, "账号名称已存在 account: {}", dto.getLoginName());
                throw new BusinessException(I18nConstants.DATA_EXISTS);
            }
        }
    }

    @Override
    public List<UserPowerVO> getResourceModuleList(Integer userId) {
        Map<Integer, List<RoleEntity>> roleListMap = roleService.getRoleMap(ListUtil.of(userId));
        List<RoleEntity> roleDOList = roleListMap.get(userId);
        if (CollUtil.isEmpty(roleDOList)) {
            LogHelp.warn(log, "userId: {} 查无角色权限", userId);
            return Collections.emptyList();
        }
        List<UserPowerVO> vos = roleDOList.stream().map(i-> {
            UserPowerVO vo = new UserPowerVO();
            vo.setRoleId(i.getId());
            vo.setCode(i.getRoleCode());
            return vo;
        }).collect(Collectors.toList());

        Set<Integer> roleSet = vos.stream().map(UserPowerVO::getRoleId).collect(Collectors.toSet());
        Map<Integer, List<String>> keyRoleIdValueMenuCode = menuService
                .getMenuCodeListMap(new ArrayList<>(roleSet));
        for (UserPowerVO vo : vos) {
            String code = vo.getCode();
            if (ObjectUtil.equals(GlobalConstant.SUPER_ADMIN_ROLE, code)) {
                List<String> allCodeList = menuService.getAllCodeList();
                vo.setModules(allCodeList);
                continue;
            }
            List<String> menuCodeList = keyRoleIdValueMenuCode.get(vo.getRoleId());
            if (CollUtil.isEmpty(menuCodeList)) {
                LogHelp.warn(log, "userId: {} roleId: {} 查无模块权限", userId, vo.getRoleId());
                menuCodeList = Collections.emptyList();
            }
            vo.setModules(menuCodeList);
        }
        return vos;
    }

    @Override
    public Map<Integer, UserEntity> getEntityMap(List<Integer> userIdList) {
        List<UserEntity> list = this.list(userIdList);
        return list.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
    }

    @Override
    public Map<Integer, String> getNameMap(List<Integer> userIdList) {
        List<UserEntity> list = this.list(userIdList);
        return list.stream().collect(Collectors.toMap(IdEntity::getId, UserEntity::getActualName));
    }

    @Override
    public UserEntity get(Integer userId) {
        if (ObjUtil.isNotNull(userId)) {
            List<UserEntity> list = this.list(ListUtil.of(userId));
            if (CollUtil.isNotEmpty(list)) {
                return list.get(0);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetPassword(Integer userId) {
        String newPassword = RandomUtil.genAz09(5);
        baseRepository.update(userId, this.buildPassword(newPassword));
        return newPassword;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetPassword(Integer userId, String newPassword) {
        baseRepository.update(userId, this.buildPassword(newPassword));
        return newPassword;
    }

    @Override
    public List<UserAllVO> queryAll(Boolean disabledFlag) {
        List<UserAllVO> voList = new ArrayList<>();
        List<UserEntity> list = baseRepository.list();
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> deptIdSet = list.stream()
                    .map(UserEntity::getDepartmentId)
                    .filter(ObjUtil::isNotNull).collect(Collectors.toSet());
            List<DepartmentEntity> departmentEntities = departmentRepository
                    .listByIds(new ArrayList<>(deptIdSet));
            if (CollUtil.isEmpty(departmentEntities)) {
                return Collections.emptyList();
            }
            Map<Integer, DepartmentEntity> deptMap = departmentEntities.stream()
                    .collect(Collectors.toMap(IdEntity::getId, Function.identity()));
            List<Integer> userIdList = list.stream().map(IdEntity::getId).collect(Collectors.toList());
            Map<Integer, List<RoleEntity>> roleMap = roleService.getRoleMap(userIdList);

            for (UserEntity userEntity : list) {
                UserAllVO vo = UserConvert.entityToAllVO(userEntity);
                Integer departmentId = userEntity.getDepartmentId();
                if (ObjUtil.isNotNull(departmentId)) {
                    DepartmentEntity departmentEntity = deptMap.get(departmentId);
                    if (ObjUtil.isNotNull(departmentEntity)) {
                        vo.setDepartmentName(departmentEntity.getName());
                    }
                }
                if (MapUtil.isNotEmpty(roleMap)) {
                    List<RoleEntity> roleEntities = roleMap.get(userEntity.getId());
                    if (CollUtil.isNotEmpty(roleEntities)) {
                        vo.setRoleIdList(roleEntities.stream()
                                .map(IdEntity::getId).collect(Collectors.toList()));
                        vo.setRoleNameList(roleEntities.stream()
                                .map(RoleEntity::getRoleName).collect(Collectors.toList()));
                    }
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchUpdateDepartment(UserBatchUpdateDepartmentDTO dto) {
        List<Integer> list = dto.getEmployeeIdList();
        List<UserEntity> userEntities = baseRepository.listByIds(list);
        if (list.size() != userEntities.size()) {
            this.throwDataNotExistException(list);
        }
        Integer departmentId = dto.getDepartmentId();
        userEntities.forEach(i -> i.setDepartmentId(departmentId));
        baseRepository.updateBatchById(userEntities);
    }

    @Override
    public PagedVO<UserVO> query(RoleUserQueryDTO dto) {
        List<Integer> idList = userRoleService.getUserId(dto.getRoleId());
        if (CollUtil.isEmpty(idList)) {
            return PageUtil.empty(dto);
        }
        Page<UserEntity> entityPage = baseRepository.selectPageByRoleId(idList, dto);
        List<UserEntity> userList = entityPage.getRecords();
        if (CollUtil.isEmpty(userList)) {
            return PageUtil.empty(dto);
        }
        return PageUtil.toPage(this.getVoList( userList), entityPage);
    }

    private List<UserVO> getVoList(List<UserEntity> userList) {
        List<UserVO> voList = new ArrayList<>();
        Set<Integer> createdBySet = userList.stream().map(BaseEntity::getCreatedBy)
                .collect(Collectors.toSet());
        Set<Integer> updatedBySet = userList.stream().map(BaseEntity::getUpdatedBy)
                .filter(ObjUtil::isNotNull).collect(Collectors.toSet());
        createdBySet.addAll(updatedBySet);
        List<UserEntity> list = baseRepository.listByIds(createdBySet);
        Map<Integer, UserEntity> userMap = new HashMap<>(list.size());
        if (CollUtil.isNotEmpty(list)) {
            userMap = list.stream().collect(Collectors.toMap(IdEntity::getId, Function.identity()));
        }
        List<Integer> userIdList = userList.stream().map(IdEntity::getId).collect(Collectors.toList());
        Map<Integer, List<RoleEntity>> roleListMap = roleService.getRoleMap(userIdList);
        Set<Integer> depIdSet = userList.stream().map(UserEntity::getDepartmentId)
                .collect(Collectors.toSet());
        List<DepartmentEntity> departmentEntities = departmentRepository.listByIds(depIdSet);
        Map<Integer, DepartmentEntity> deptMap = departmentEntities.stream()
                .collect(Collectors.toMap(IdEntity::getId, Function.identity()));

        for (UserEntity entity : userList) {
            UserVO vo = UserConvert.entityToVO(entity);
            this.setRoleListFieldValue(roleListMap, vo, entity.getId());
            this.setDeptFieldValue(deptMap, entity.getDepartmentId(),vo);
            this.setCreatedByFieldValue(userMap, entity.getCreatedBy(), vo);
            this.setUpdatedByFieldValue(userMap, entity.getUpdatedBy(), vo);
            voList.add(vo);
        }
        return voList;
    }

    private void setUpdatedByFieldValue(Map<Integer, UserEntity> userMap,
                                        Integer updatedBy,
                                        UserVO vo) {
        if (ObjUtil.isNotNull(updatedBy)) {
            UserEntity userEntity = userMap.get(updatedBy);
            if (ObjUtil.isNotNull(userEntity)) {
                vo.setUpdatedByName(userEntity.getActualName());
            }
        }
    }

    private void setCreatedByFieldValue(Map<Integer, UserEntity> userMap,
                                        Integer createdBy,
                                        UserVO vo) {
        if (ObjUtil.isNotNull(createdBy)) {
            UserEntity userEntity = userMap.get(createdBy);
            if (ObjUtil.isNotNull(userEntity)) {
                vo.setCreatedByName(userEntity.getActualName());
            }
        }
    }

    private void setDeptFieldValue(Map<Integer, DepartmentEntity> deptMap,
                                   Integer departmentId,
                                   UserVO vo) {
        if (ObjUtil.isNotNull(departmentId)) {
            DepartmentEntity departmentEntity = deptMap.get(departmentId);
            if (ObjUtil.isNotNull(departmentEntity)) {
                vo.setDepartmentName(departmentEntity.getName());
            }
        }
    }


    @Override
    public List<UserVO> getAllByRoleId(Integer roleId) {
        List<Integer> idList = userRoleService.getUserId(roleId);
        if (CollUtil.isEmpty(idList)) {
            return Collections.emptyList();
        }
        PagedDTO dto = new PagedDTO();
        dto.setNotNeedPaged(true);
        Page<UserEntity> entityPage = baseRepository.selectPageByRoleId(idList, dto);
        List<UserEntity> records = entityPage.getRecords();
        return this.getVoList(records);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserListByRole(RoleUserUpdateDTO dto) {
        userRoleService.batchUserListByRoleId(dto.getEmployeeIdList(), dto.getRoleId());
    }

    @Override
    public List<UserEntity> listByDeptList(List<Integer> deptIdList) {
        return baseRepository.listByDeptList(deptIdList);
    }

    @Override
    public List<UserEntity> getLike(String userName) {
        return baseRepository.like(userName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLoginTime(Integer id) {
        UserEntity entity = (UserEntity) this.checkDataExistById(id);
        entity.setLastLoginTime(UserContextHolder.getSession().getNow());
        baseRepository.updateById(entity);
    }

    @Override
    public PagedVO<UserVO> pageByRoleId(Integer roleId, UserListDTO dto) {
        List<Integer> userId = userRoleService.getUserId(roleId);
        Page<UserEntity> entityPage = baseRepository.selectPage(dto, null, userId);
        List<UserVO> voList = new ArrayList<>();
        List<UserEntity> userList = entityPage.getRecords();
        if (entityPage.getTotal() == GlobalConstant.Number.NUMBER_0) {
            return PageUtil.toPage(voList, entityPage);
        }
        voList = getVoList(userList);
        return PageUtil.toPage(voList, entityPage);
    }

    private List<UserEntity> list(List<Integer> userIdList) {
        if (CollUtil.isNotEmpty(userIdList)) {
            List<UserEntity> userList = baseRepository.listByIds(userIdList);
            return CollUtil.emptyIfNull(userList);
        }
        return Collections.emptyList();
    }

    @Override
    public String getActualName(Integer userId) {
        if (ObjUtil.isNotNull(userId)) {
            UserEntity user = baseRepository.getById(userId);
            if (ObjUtil.isNotNull(user)) {
                return user.getActualName();
            }
        }
        return StrUtil.EMPTY;
    }
}
