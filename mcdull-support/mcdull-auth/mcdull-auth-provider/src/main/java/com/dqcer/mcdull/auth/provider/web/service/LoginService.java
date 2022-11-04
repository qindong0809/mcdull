package com.dqcer.mcdull.auth.provider.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dqcer.framework.base.enums.DelFlayEnum;
import com.dqcer.framework.base.enums.StatusEnum;
import com.dqcer.framework.base.utils.Sha1Util;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.auth.api.dto.LoginDTO;
import com.dqcer.mcdull.auth.api.entity.SysUserEntity;
import com.dqcer.mcdull.auth.api.vo.LoginVO;
import com.dqcer.mcdull.auth.provider.constants.AuthCode;
import com.dqcer.mcdull.auth.provider.web.dao.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private UserDAO userDAO;

    public Result<LoginVO> login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        LambdaQueryWrapper<SysUserEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.ge(SysUserEntity::getAccount, account);
        SysUserEntity entity = userDAO.selectOne(wrapper);
        if (null == entity) {
            log.warn("账号不存在 account: {}", account);
            return Result.error(AuthCode.NOT_EXIST);
        }
        String password = entity.getPassword();

        if (!password.equals(Sha1Util.getSha1(loginDTO.getPd() + entity.getSalt()))) {
            log.warn("用户密码错误");
            return Result.error(AuthCode.NOT_EXIST);
        }
        if (!entity.getStatus().equals(StatusEnum.ENABLE.getCode())) {
            log.warn("账号已停用 account: {}", account);
            return Result.error(AuthCode.DISABLE);
        }
        if (!entity.getDefFlay().equals(DelFlayEnum.NORMAL.getCode())) {
            log.warn("账号已被删除 account: {}", account);
            return Result.error(AuthCode.NOT_EXIST);
        }

        return null;
    }
}
