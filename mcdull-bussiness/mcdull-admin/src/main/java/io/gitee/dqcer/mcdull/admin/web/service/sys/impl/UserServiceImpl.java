package io.gitee.dqcer.mcdull.admin.web.service.sys.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.admin.framework.transformer.IUserTransformerService;
import io.gitee.dqcer.mcdull.admin.model.convert.sys.UserConvert;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEmailConfigDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.entity.sys.*;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.model.enums.UserTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserEmailConfigVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserProfileVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.util.EmailUtil;
import io.gitee.dqcer.mcdull.admin.util.LogHelpUtil;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.sys.*;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.manager.sys.IUserManager;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IUserService;
import io.gitee.dqcer.mcdull.business.common.FileNameGeneratorUtil;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.enums.StatusEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.util.Md5Util;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.util.RandomUtil;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.base.vo.BaseVO;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.CodeEnum;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.base.wrapper.ResultParse;
import io.gitee.dqcer.mcdull.framework.config.properties.MailProperties;
import io.gitee.dqcer.mcdull.framework.config.properties.McdullProperties;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务 impl
 *
 * @author dqcer
 * @since 2023/01/15 13:01:30
 */
@Service
public class UserServiceImpl extends BasicServiceImpl<IUserRepository> implements IUserService, IUserTransformerService {

    @Resource
    private IUserManager userManager;

    @Resource
    private IRoleRepository repository;

    @Resource
    private IPostRepository postRepository;

    @Resource
    private IUserRoleRepository userRoleRepository;

    @Resource
    private IUserPostRepository userPostRepository;

    @Resource
    private ISysConfigManager sysConfigManager;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private McdullProperties mcdullProperties;

    @Resource
    private IDeptRepository deptRepository;

    @Resource
    private IUserEmailConfigRepository userEmailConfigRepository;

    @Resource
    private IUserRepository userRepository;

    @Resource
    private IRoleRepository roleRepository;

    @Transform
    @Override
    public Result<PagedVO<UserVO>> paged(UserLiteDTO dto) {
        Result<PagedVO<UserVO>> result = this.list(dto);
        this.buildPagedLog(dto);
        return result;
    }

    public Result<PagedVO<UserVO>> list(UserLiteDTO dto) {
        Page<UserEntity> entityPage = baseRepository.selectPage(dto);
        List<UserVO> voList = new ArrayList<>();
        for (UserEntity entity : entityPage.getRecords()) {
            UserVO vo = userManager.entityToVo(entity);
            voList.add(vo);
        }
        return Result.success(PageUtil.toPage(voList, entityPage));
    }



    private void buildPagedLog(UserLiteDTO dto) {
        String logDesc = "第{}页,查询条数{}";
        LogHelpUtil.setLog(StrUtil.format(logDesc, dto.getPageNum(), dto.getPageSize()));
    }

    /**
     * 单个详情
     *
     * @param userId userId
     * @return {@link Result<UserVO>}
     */
    @Override
    public Result<UserDetailVO> detail(Long userId) {
        UserDetailVO vo = new UserDetailVO();
        if (ObjUtil.isNotNull(userId)) {
            UserEntity userDO = baseRepository.getById(userId);
            vo = userManager.entityToDetailVo(userDO);
            vo.setPosts(this.getPostBaseList());
            vo.setRoles(this.getRoleBaseList());
            this.buildDetailLog(vo);
            return Result.success(vo);
        }
        vo.setPosts(this.getPostBaseList());
        vo.setRoles(this.getRoleBaseList());
        return Result.success(vo);
    }

    private String buildLogIndex() {
        return "用户:{}";
    }

    private void buildDetailLog(UserDetailVO vo) {
        String logDesc = this.buildLogIndex();
        LogHelpUtil.setLog(StrUtil.format(logDesc, vo.getNickName()));
    }

    private List<BaseVO<Long, String>> getRoleBaseList() {
        List<RoleEntity> roleList = repository.getAll();
        List<BaseVO<Long, String>> roleBaseList = new ArrayList<>();
        for (RoleEntity roleDO : roleList) {
            BaseVO<Long, String> baseVO = new BaseVO<>();
            baseVO.setId(roleDO.getId());
            baseVO.setName(roleDO.getName());
            roleBaseList.add(baseVO);
        }
        return roleBaseList;
    }

    private List<BaseVO<Long, String>> getPostBaseList() {
        List<PostEntity> postList = postRepository.getAll();
        List<BaseVO<Long, String>> postBaseList = new ArrayList<>();
        for (PostEntity postDO : postList) {
            BaseVO<Long, String> baseVO = new BaseVO<>();
            baseVO.setId(postDO.getId());
            baseVO.setName(postDO.getPostName());
            postBaseList.add(baseVO);
        }
        return postBaseList;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> add(UserInsertDTO dto) {
        List<UserEntity> list = this.validName(null, dto.getAccount());
        Long userId;
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }
        UserEntity entity = UserConvert.dtoToEntity(dto);
        String salt = RandomUtil.uuid();
        String password = Sha1Util.getSha1(Md5Util.getMd5(dto.getAccount() + salt));
        entity.setSalt(salt);
        entity.setPassword(password);
        entity.setType(UserTypeEnum.READ_WRITE.getCode());
        userId = baseRepository.insert(entity);
        userRoleRepository.updateByUserId(userId, dto.getRoleIds());
        userPostRepository.updateByUserId(userId, dto.getPostIds());

        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.CREATED_USER_SEND_EMAIL);
        boolean isSendEmail = Convert.toBool(valueByEnum);
        if (isSendEmail) {
            this.sendEmail(entity);
        }
        this.buildAddOrEditLog(dto);
        return Result.success(userId);
    }

    @SneakyThrows(Exception.class)
    private void sendEmail(UserEntity entity) {
        Long currentUserId = UserContextHolder.currentUserId();
        UserEntity currentUser = this.baseRepository.getById(currentUserId);

        MailProperties defaultMailProperties = mcdullProperties.getMail();
        String host = defaultMailProperties.getHost();
        String username = defaultMailProperties.getUsername();
        String password = defaultMailProperties.getPassword();
        Integer port = defaultMailProperties.getPort();

        boolean hasMailConfig = false;
        if (hasMailConfig) {
            // TODO: 2023/8/21
        }

        ClassPathResource resource = new ClassPathResource("email/Account-Create-Ok-Template.html");
        String emailTemplate = IoUtil.readUtf8(resource.getStream());
        String textHtml = StrUtil.format(emailTemplate, entity.getNickName(), entity.getAccount(), entity.getAccount(), currentUser.getNickName());
        String subject = "Mcdull系统账号开通提示";
        threadPoolTaskExecutor.submit(() ->
                EmailUtil.send(host, username, password, port,
                        ListUtil.of(entity.getEmail()), new ArrayList<>(), subject, textHtml, true));
    }

    private void buildAddOrEditLog(UserInsertDTO dto) {
        String logDesc = this.buildLogIndex();
        LogHelpUtil.setLog(StrUtil.format(logDesc, dto.getAccount()));
    }

    private List<UserEntity> validName(Long userId, String account) {
        LambdaQueryWrapper<UserEntity> query = Wrappers.lambdaQuery();
        query.eq(UserEntity::getAccount, account);
        query.ne(ObjUtil.isNotNull(userId), IdEntity::getId, userId);
        query.last(GlobalConstant.Database.SQL_LIMIT_1);
        return baseRepository.list(query);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> edit(UserEditDTO dto) {
        Long userId = dto.getId();
        List<UserEntity> list = this.validName(userId, dto.getAccount());
        if (!list.isEmpty()) {
            return Result.error(CodeEnum.DATA_EXIST);
        }

        UserEntity userDO = baseRepository.getById(userId);
        userDO.setDeptId(dto.getDeptId());
        userDO.setPhone(dto.getPhone());
        userDO.setEmail(dto.getEmail());
        userDO.setNickName(dto.getNickName());
        userDO.setAccount(dto.getAccount());
        userDO.setStatus(dto.getStatus());
        baseRepository.updateById(userDO);
        userRoleRepository.updateByUserId(userId, dto.getRoleIds());
        userPostRepository.updateByUserId(userId, dto.getPostIds());

        this.buildAddOrEditLog(dto);
        return Result.success(userId);
    }

    /**
     * 更新状态
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> updateStatus(StatusDTO dto) {
        Long id = dto.getId();
        UserEntity userDO = baseRepository.getById(id);
        if (ObjUtil.isNull(userDO)) {
            throw new BusinessException(CodeEnum.DATA_NOT_EXIST);
        }
        baseRepository.updateStatusById(id, dto.getStatus());

        String logDesc = this.buildLogIndex() + "<状态: {}更新为{}>";
        logDesc = StrUtil.format(logDesc, userDO.getNickName(),
                IEnum.getByCode(StatusEnum.class, userDO.getStatus()).getText(), IEnum.getByCode(StatusEnum.class, dto.getStatus()).getText());
        LogHelpUtil.setLog(logDesc);
        return Result.success(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> delete(Long id) {
        this.validDeleteParam(id);
        baseRepository.delete(id);
        this.buildDeleteLog(id);
        return Result.success(id);
    }

    private void buildDeleteLog(Long id) {
        UserEntity userDO = baseRepository.getById(id);
        String logDesc = this.buildLogIndex();
        LogHelpUtil.setLog(StrUtil.format(logDesc, userDO.getNickName()));
    }

    private void validDeleteParam(Long id) {
        Long currentUserId = UserContextHolder.currentUserId();
        if (id.equals(currentUserId)) {
            throw new BusinessException("不能对自己的账号进行删除");
        }
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Override
    public Result<Long> resetPassword(UserLiteDTO dto) {
        return null;
    }

    @SneakyThrows
    @Override
    public void export(UserLiteDTO dto) {
        dto.setNotNeedPaged(true);
        Result<PagedVO<UserVO>> pagedVOResult = this.list(dto);
        List<UserVO> list = ResultParse.getPageData(pagedVOResult, Result::getData);

        HttpServletResponse response = ServletUtil.getResponse();
        String fileName = FileNameGeneratorUtil.simple("用户信息");
        ServletUtil.setDownloadExcelHttpHeader(response, fileName);
        EasyExcel.write(response.getOutputStream(),  UserVO.class).sheet().doWrite(list);

        this.buildExportLog(fileName);
    }

    @Transactional(readOnly = true)
    @Override
    public Result<UserProfileVO> profile() {
        Long userId = UserContextHolder.currentUserId();
        UserEntity userInfo = baseRepository.getById(userId);
        UserProfileVO vo = UserConvert.toUserProfileVO(userInfo);
        DeptEntity deptInfo = deptRepository.getById(userInfo.getDeptId());
        vo.setDeptName(deptInfo.getName());
        if (UserTypeEnum.READ_ONLY.getCode().equals(userInfo.getType())) {
            vo.setIsAdmin(true);
        }

        List<Long> roleIdList = userRoleRepository.listRoleByUserId(userId);
        if (CollUtil.isNotEmpty(roleIdList)) {
            List<RoleEntity> roleList = roleRepository.queryListByIds(roleIdList);
            if (CollUtil.isNotEmpty(roleList)) {
                String collect = roleList.stream().map(RoleEntity::getName).collect(Collectors.joining(","));
                vo.setRoleNameList(collect);
            }
        }
        return Result.success(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> updateEmailConfig(UserEmailConfigDTO dto) {
        Long userId = UserContextHolder.currentUserId();
        UserEmailConfigEntity dbUserEmailConfig = userEmailConfigRepository.getOneByUserId(userId);
        if (ObjUtil.isNull(dbUserEmailConfig)) {
            UserEmailConfigEntity userEmailConfig = UserConvert.toEmailConfigDO(dto);
            userEmailConfig.setUserId(userId);
            userEmailConfigRepository.save(userEmailConfig);
        } else {
            UserEmailConfigEntity userEmailConfig = UserConvert.toEmailConfigDO(dto);
            userEmailConfig.setId(dbUserEmailConfig.getId());
            userEmailConfigRepository.updateById(userEmailConfig);
        }
        return Result.success(true);
    }

    @Transactional(readOnly = true)
    @Override
    public Result<UserEmailConfigVO> detailEmailConfig() {
        Long userId = UserContextHolder.currentUserId();
        UserEmailConfigEntity dbUserEmailConfig = userEmailConfigRepository.getOneByUserId(userId);
        UserEmailConfigVO vo = UserConvert.toEmailConfigVO(dbUserEmailConfig);
        return Result.success(vo);
    }

    @Override
    public Result<Boolean> testEmailConfig(UserEmailConfigDTO dto) {
        Long currentUserId = UserContextHolder.currentUserId();
        UserEntity entity = this.baseRepository.getById(currentUserId);
        threadPoolTaskExecutor.submit(() -> {
            try {
                EmailUtil.send(dto.getHost(), dto.getUsername(), dto.getPassword(), dto.getPort(),
                        ListUtil.of(entity.getEmail()), new ArrayList<>(), "这是一封测试邮件", "测试Email配置是否正常", false);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return Result.success(true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Long> updatePwd(String oldPassword, String newPassword) {
        Long userId = UserContextHolder.currentUserId();
        UserEntity user = userRepository.getById(userId);
        String password = Sha1Util.getSha1(Md5Util.getMd5(oldPassword + user.getSalt()));
        if (StrUtil.isNotBlank(password)) {
            if (!password.equals(oldPassword)) {
                return Result.error("验证失败");
            }
        }
        newPassword = Sha1Util.getSha1(Md5Util.getMd5(newPassword + user.getSalt()));
        user.setPassword(newPassword);
        userRepository.updateById(user);
        return Result.success(userId);
    }

    private void buildExportLog(String fileName) {
        String logDesc = "导出文件:{}";
        LogHelpUtil.setLog(StrUtil.format(logDesc, fileName));
    }

    /**
     * 翻译
     *
     * @param code  编码
     * @return {@link KeyValueVO}
     */
    @Override
    public KeyValueBO<String, String> transformer(String code) {
        UserEntity userDO = baseRepository.getById(code);
        if (ObjUtil.isNotNull(userDO)) {
            return new KeyValueBO<String, String>().setKey(userDO.getId().toString()).setValue(userDO.getNickName());
        }
        return new KeyValueBO<>();
    }
}
