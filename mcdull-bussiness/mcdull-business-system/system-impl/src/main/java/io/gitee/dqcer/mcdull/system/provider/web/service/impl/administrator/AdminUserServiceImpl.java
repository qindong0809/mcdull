package io.gitee.dqcer.mcdull.system.provider.web.service.impl.administrator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.Sha1Util;
import io.gitee.dqcer.mcdull.framework.security.StpKit;
import io.gitee.dqcer.mcdull.system.provider.model.dto.administrator.LogonDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.administrator.AdminUserEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.mapper.administrator.AdminUserMapper;
import io.gitee.dqcer.mcdull.system.provider.web.service.administrator.IAdminUserService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICaptchaService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Date;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserEntity> implements IAdminUserService {

    @Resource
    private ICaptchaService captchaService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String auth(@Valid LogonDTO dto) {
        // 1. 验证验证码
        this.validateCaptcha(dto.getCaptcha(), dto.getUuid());
        
        // 2. 查询管理员用户
        AdminUserEntity adminUser = this.getUserByLoginName(dto.getLoginName());
        if (ObjUtil.isNull(adminUser)) {
            throw new BusinessException("管理员账号不存在");
        }
        
        // 3. 验证密码
        this.validatePassword(adminUser, dto.getLoginPwd());
        
        // 4. 检查账号状态
        this.validateAccountStatus(adminUser);
        
        // 5. 执行登录
        StpKit.ADMIN.login(adminUser.getId());
        
        // 6. 更新最后登录时间
        this.updateLastLoginTime(adminUser.getId());
        
        // 7. 返回token
        return StpUtil.getTokenValue();
    }
    
    /**
     * 验证验证码
     */
    private void validateCaptcha(String captcha, String uuid) {
        if (StrUtil.isBlank(captcha) || StrUtil.isBlank(uuid)) {
            throw new BusinessException("验证码不能为空");
        }
        captchaService.checkCaptcha(captcha, uuid);
    }
    
    /**
     * 验证密码
     */
    private void validatePassword(AdminUserEntity adminUser, String inputPassword) {
        if (StrUtil.isBlank(inputPassword)) {
            throw new BusinessException("密码不能为空");
        }
        
        // 对输入密码进行SHA1加密后与数据库中的密码比较
        String encryptedPassword = Sha1Util.getSha1(inputPassword);
        if (!adminUser.getLoginPwd().equals(encryptedPassword)) {
            throw new BusinessException("用户名或密码错误");
        }
    }
    
    /**
     * 验证账号状态
     */
    private void validateAccountStatus(AdminUserEntity adminUser) {
        if (Boolean.FALSE.equals(adminUser.getAdministratorFlag())) {
            throw new BusinessException("账号已被禁用");
        }
    }
    
    /**
     * 更新最后登录时间
     */
    private void updateLastLoginTime(Integer userId) {
        AdminUserEntity updateEntity = new AdminUserEntity();
        updateEntity.setId(userId);
        updateEntity.setLastLoginTime(new Date());
        this.updateById(updateEntity);
    }

    private AdminUserEntity getUserByLoginName(String loginName) {
        LambdaQueryWrapper<AdminUserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdminUserEntity::getLoginName, loginName);
        return this.getOne(queryWrapper);
    }
}
