package io.gitee.dqcer.mcdull.admin.model.convert.sys;

import io.gitee.dqcer.mcdull.admin.model.entity.sys.UserLoginEntity;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LoginInfoVO;

/**
* 登录信息 对象转换工具类
*
* @author dqcer
* @since 2023-01-18
*/
public class LoginInfoConvert {

    public static LoginInfoVO convertToLoginInfoVO(UserLoginEntity item){
        LoginInfoVO vo = new LoginInfoVO();
        vo.setId(item.getId());
        vo.setCreatedTime(item.getCreatedTime());
        vo.setAccount(item.getAccount());
        vo.setType(item.getType());
        vo.setBrowser(item.getBrowser());
        vo.setOs(item.getOs());
        vo.setRemark(item.getRemark());
        vo.setOperationType(item.getOperationType());
        return vo;
    }

}