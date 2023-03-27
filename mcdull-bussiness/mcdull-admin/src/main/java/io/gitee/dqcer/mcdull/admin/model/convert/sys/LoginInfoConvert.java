package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginDO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LoginInfoVO;

/**
* 登录信息 对象转换工具类
*
* @author dqcer
* @since 2023-01-18
*/
public class LoginInfoConvert {

    public static LoginInfoVO convertToLoginInfoVO(UserLoginDO item){
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setId(item.getId());
        loginInfoVO.setCreatedTime(item.getCreatedTime());
        loginInfoVO.setUserId(item.getUserId());
        loginInfoVO.setToken(item.getToken());
        loginInfoVO.setType(item.getType());
        return loginInfoVO;
    }

}