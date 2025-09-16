package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.audit.Audit;
import io.gitee.dqcer.mcdull.business.common.excel.DataAnalysisListener;
import io.gitee.dqcer.mcdull.business.common.excel.DynamicFieldTemplate;
import io.gitee.dqcer.mcdull.business.common.pdf.ByteArrayInOutConvert;
import io.gitee.dqcer.mcdull.business.common.pdf.HtmlConvertPdf;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.web.enums.InactiveEnum;
import io.gitee.dqcer.mcdull.framework.web.enums.SexEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import io.gitee.dqcer.mcdull.system.provider.model.audit.UserAudit;
import io.gitee.dqcer.mcdull.system.provider.model.bo.DynamicFieldBO;
import io.gitee.dqcer.mcdull.system.provider.model.convert.UserConvert;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.entity.DepartmentEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.RoleEntity;
import io.gitee.dqcer.mcdull.system.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.system.provider.model.enums.DictSelectTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.EmailTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.enums.FormItemControlTypeEnum;
import io.gitee.dqcer.mcdull.system.provider.model.vo.RoleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserAllVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IDepartmentRepository;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IUserRepository;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IAuditManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IDictTypeManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.*;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    @Resource
    private IAuditManager auditManager;
    @Resource
    private IDictTypeManager dictTypeManager;
    @Resource
    private IEmailService emailService;
    @Resource
    private ICommonManager commonManager;
    @Resource
    private IFileService fileService;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private IFolderService folderService;

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

    private String hash(String webPassword) {
        return Sha1Util.getSha1(decrypt(webPassword));
    }

    public String decrypt(String data) {
        try {
            // 第一步： Base64 解码
            byte[] base64Decode = Base64.getDecoder().decode(data);
            // 第二步： AES 解密
            AES aes = new AES(AES_KEY.getBytes(StandardCharsets.UTF_8));
            byte[] decryptedBytes = aes.decrypt(base64Decode);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LogHelp.error(log, "密码解密失败", e);
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
        UserEntity entity = UserConvert.insertDtoToEntity(dto);
        entity.setLoginPwd(this.getDefaultPassword());
        entity.setAdministratorFlag(false);
        baseRepository.insert(entity);
        Integer userId = entity.getId();
        threadPoolTaskExecutor.execute(() -> this.sendCreateAccountEmail(entity));
        userRoleService.batchUserListByRoleId(userId, dto.getRoleIdList());
        auditManager.saveByAddEnum(dto.getActualName(), userId, this.buildAuditLog(entity, dto.getRoleIdList()));
        return userId;
    }

    private void sendCreateAccountEmail(UserEntity entity) {
        String value = commonManager.getConfig("create-account-email");
        if (StrUtil.isNotBlank(value)) {
            String email = entity.getEmail();
            if (StrUtil.isNotBlank(email)) {
                String content = StrUtil.format(value, entity.getLoginName());
                MapBuilder<String, String> builder = MapUtil.builder();
                builder.put("{title}", StrUtil.EMPTY)
                        .put("{content}", content)
                        .put("{domainName}", commonManager.getConfig("domain-name"))
                        .put("{actualName}", entity.getActualName());
                String templateFileName = "template/common-template-email.html";
                String html = commonManager.readTemplateFileContent(templateFileName);
                String htmlStr = commonManager.replacePlaceholders(html, builder.map());
                String title = "开通账号";
                emailService.sendEmailHtml(EmailTypeEnum.CREATE_ACCOUNT, email, title,
                        htmlStr);
                this.generatePdfDocumentConditional(title, email, htmlStr, entity);
            }
        }
    }

    private void generatePdfDocumentConditional(String title, String email, String htmlStr, UserEntity entity) {
        Boolean status = commonManager.getConfigToBool("create-account-email-build-pdf");
        if (BooleanUtil.isTrue(status)) {
            ByteArrayInOutConvert byteArrayInOutStream = new HtmlConvertPdf()
                    .generatePdf(HtmlConvertPdf.getHtml(title, ListUtil.of(email), new ArrayList<>(), new Date(), htmlStr));
            // 第二次更新pdf
            String message =  commonManager.convertDateTimeStr(new Date());
            String fileName = StrUtil.format("{}_{}_{}.pdf", title, entity.getActualName(), message);
            String pafPathParent = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
            String pdfPath = pafPathParent + File.separator + fileName;
            HtmlConvertPdf.updatePdfLeftFooter(byteArrayInOutStream.getInputStream(), pdfPath, message);
            fileService.fileUpload(new File(pdfPath), folderService.getSystemFolderId());
            FileUtil.del(pdfPath);
            FileUtil.del(pafPathParent);
        }
    }

    private String getDefaultPassword() {
        String value = commonManager.getConfig("init-account-password");
        if (StrUtil.isNotBlank(value)) {
            return Sha1Util.getSha1(value);
        }
        return StrUtil.EMPTY;
    }

    private Audit buildAuditLog(UserEntity user, List<Integer> roleIdList) {
        UserAudit audit = new UserAudit();
        audit.setLoginName(user.getLoginName());
        audit.setActualName(user.getActualName());
        audit.setRemark(user.getRemark());
        Integer departmentId = user.getDepartmentId();
        if (ObjUtil.isNotNull(departmentId)) {
            DepartmentEntity department = departmentRepository.getById(departmentId);
            if (ObjUtil.isNotNull(department)) {
                audit.setDepartment(department.getName());
            }
        }
        Integer gender = user.getGender();
        if (ObjUtil.isNotNull(gender)) {
            KeyValueBO<String, String> vo = dictTypeManager.dictVO(DictSelectTypeEnum.USER_SEX, gender.toString());
            if (ObjUtil.isNotNull(vo)) {
                audit.setGender(vo.getValue());
            }
        }
        audit.setEmail(user.getEmail());
        audit.setPhone(user.getPhone());
        audit.setRemark(user.getRemark());
        if (CollUtil.isNotEmpty(roleIdList)) {
            Map<Integer, String> roleMap = roleService.mapName(roleIdList);
            StringJoiner joiner = new StringJoiner(StrUtil.COMMA + StrUtil.SPACE);
            roleIdList.forEach(roleId -> joiner.add(roleMap.get(roleId)));
            audit.setRoleJoin(joiner.toString());
        }

        return audit;
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
        auditManager.saveByStatusEnum(dbData.getActualName(), id, !dbData.getInactive(), null);
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
        baseRepository.removeByIds(idList);
        for (UserEntity userEntity : userEntities) {
            auditManager.saveByDeleteEnum(userEntity.getActualName(), userEntity.getId(), null);
        }
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
        Map<Integer, List<Integer>> roleIdListMap = userRoleService.getRoleIdListMap(ListUtil.of(id));
        List<Integer> roleIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(roleIdListMap)) {
            roleIdList = roleIdListMap.get(id);
        }
        UserEntity updateDO = UserConvert.updateDtoToEntity(dto);
        updateDO.setId(id);
        baseRepository.updateById(updateDO);
        userRoleService.batchUserListByRoleId(id, dto.getRoleIdList());
        auditManager.saveByUpdateEnum(entity.getActualName(), entity.getId(),
                this.buildAuditLog(entity, roleIdList), this.buildAuditLog(baseRepository.getById(id), dto.getRoleIdList()));
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
        UserEntity byId = baseRepository.getById(userId);
        if (ObjectUtil.isNotEmpty(byId)) {
            if (byId.getAdministratorFlag()) {
                throw new BusinessException(I18nConstants.PERMISSION_DENIED);
            }
        }
        baseRepository.update(userId, this.getDefaultPassword());
        return StrUtil.EMPTY;
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
        baseRepository.updateBatchById(userEntities, 500);
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
        List<Integer> userIdList = userList.stream().map(IdEntity::getId)
                .collect(Collectors.toList());
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
        List<UserEntity> records = baseRepository.listByIds(idList);
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
        UserEntity entity = this.mustGet(id, UserEntity.class);
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

    @Override
    public void exportData() {
        commonManager.exportExcel(new UserListDTO(), this::listByPage, StrUtil.EMPTY, this.getTitleList());
    }

    private List<Pair<String, Func1<UserVO, ?>>> getTitleList() {
        return Arrays.asList(
                Pair.of("姓名", UserVO::getActualName),
                Pair.of("登录账号", UserVO::getLoginName),
                Pair.of("性别", UserVO::getGender),
                Pair.of("手机号", UserVO::getPhone),
                Pair.of("邮箱", UserVO::getEmail),
                Pair.of("部门名称", UserVO::getDepartmentName),
                Pair.of("角色名称", UserVO::getRoleNameList),
                Pair.of("创建人", UserVO::getCreatedByName),
                Pair.of("创建时间", UserVO::getCreatedTime),
                Pair.of("更新人", UserVO::getUpdatedByName),
                Pair.of("更新时间", UserVO::getUpdatedTime),
                Pair.of("状态", UserVO::getInactive)
        );
    }

    @Override
    public void downloadTemplate() {
        Map<String, List<DynamicFieldBO>> sheetHeaderMap = new HashMap<>(8);
        List<DynamicFieldBO> fieldList = new ArrayList<>();
        fieldList.add(new DynamicFieldBO("username", "姓名", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(this.getSexFieldBO());
        fieldList.add(new DynamicFieldBO("loginName", "登录账号", true, FormItemControlTypeEnum.INPUT));
        fieldList.add(this.getStatusFieldBO());
        fieldList.add(this.getRoleFieldBO());
        fieldList.add(this.getDepartmentFieldBO());
        fieldList.add(new DynamicFieldBO("email", "邮箱", true, FormItemControlTypeEnum.INPUT));
        sheetHeaderMap.put("模板", fieldList);
        commonManager.downloadExcelTemplate(sheetHeaderMap, "部门人员模板");
    }

    private DynamicFieldBO getStatusFieldBO() {
        DynamicFieldBO statusBO = new DynamicFieldBO("status", "状态", true, FormItemControlTypeEnum.SELECT);
        statusBO.setDropdownList(IEnum.getAll(InactiveEnum.class).stream().map(IEnum::getText).collect(Collectors.toList()));
        statusBO.setExtraObj(IEnum.getAll(SexEnum.class));
        return statusBO;
    }

    private DynamicFieldBO getSexFieldBO() {
        DynamicFieldBO sexFieldBO = new DynamicFieldBO("sex", "性别", true, FormItemControlTypeEnum.SELECT);
        sexFieldBO.setDropdownList(IEnum.getAll(SexEnum.class).stream().map(IEnum::getText).collect(Collectors.toList()));
        sexFieldBO.setExtraObj(IEnum.getAll(SexEnum.class));
        return sexFieldBO;
    }

    private DynamicFieldBO getDepartmentFieldBO() {
        DynamicFieldBO departmentField = new DynamicFieldBO("departmentName", "部门名称", true, FormItemControlTypeEnum.SELECT);
        List<DepartmentEntity> departmentEntityList = departmentRepository.list();
        if (CollUtil.isNotEmpty(departmentEntityList)) {
            departmentField.setDropdownList(departmentEntityList.stream().map(DepartmentEntity::getName).collect(Collectors.toList()));
            departmentField.setExtraObj(departmentEntityList);
        }
        return departmentField;
    }

    private DynamicFieldBO getRoleFieldBO() {
        DynamicFieldBO roleField = new DynamicFieldBO("roleName", "角色", true, FormItemControlTypeEnum.SELECT);
        List<RoleVO> roleList = roleService.all();
        if (CollUtil.isNotEmpty(roleList)) {
            roleField.setDropdownList(roleList.stream().map(RoleVO::getRoleName).collect(Collectors.toList()));
            roleField.setExtraObj(roleList);
        }
        return roleField;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean importData(MultipartFile file) {
        commonManager.importExcelData(file, this::getFieldBOList, this::saveData);
        return true;
    }

    private void saveData(List<Pair<String, DataAnalysisListener>> pairList) {
        for (Pair<String, DataAnalysisListener> pair : pairList) {
            List<Tuple> dataList = pair.getValue().getDataList();
            if (CollUtil.isNotEmpty(dataList)) {
                for (Tuple tuple : dataList) {
                    List<Pair<DynamicFieldTemplate, String>> oneRowDataList = tuple.get(0);
                    UserAddDTO userAddDTO = new UserAddDTO();
                    userAddDTO.setActualName(this.getByKey(oneRowDataList, "actualName", String.class));
                    userAddDTO.setLoginName(this.getByKey(oneRowDataList, "loginName", String.class));
                    userAddDTO.setGender(this.getByKey(oneRowDataList, "sex", Integer.class));
                    userAddDTO.setDepartmentId(this.getByKey(oneRowDataList, "departmentName", Integer.class));
                    userAddDTO.setPhone(this.getByKey(oneRowDataList, "phone", String.class));
                    userAddDTO.setEmail(this.getByKey(oneRowDataList, "email", String.class));
                    userAddDTO.setDisabledFlag(this.getByKey(oneRowDataList, "inactive", Boolean.class));
                    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                    Validator validator = factory.getValidator();
                    Set<ConstraintViolation<UserAddDTO>> violations = validator.validate(userAddDTO);
                    if (!violations.isEmpty()) {
                        // 处理验证错误
                        log.error("验证错误：{}", violations);
                        continue;
                    }
                    this.insert(userAddDTO);
                }
            }
        }
    }

    private <T> T getByKey(List<Pair<DynamicFieldTemplate, String>> oneRowDataList, String key, Class<T> aClass) {
        for (Pair<DynamicFieldTemplate, String> pair : oneRowDataList) {
            DynamicFieldTemplate dynamicFieldTemplate = pair.getKey();
            if (StrUtil.equalsIgnoreCase(key, dynamicFieldTemplate.getCode())) {
                return Convert.convert(aClass, pair.getValue());
            }
        }
        return null;
    }

    private List<DynamicFieldTemplate> getFieldBOList(String fileName) {
        List<DynamicFieldTemplate> fieldBOList = new ArrayList<>();
        switch (fileName) {
            case "部门人员模板":
            default:
                break;
        }
        return fieldBOList;
    }



}
