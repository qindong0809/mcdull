package io.gitee.dqcer.mcdull.uac.provider.flow.login;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Dict;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.flow.node.ProcessHandler;
import io.gitee.dqcer.mcdull.framework.flow.node.TreeNode;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.LoginDeviceEnum;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "Authority")
@TreeNode(code = "Authority")
public class Authority implements ProcessHandler<LoginContext> {

    @Resource
    private ILoginService loginService;

    @Override
    public void execute(LoginContext context) {
        LoginDTO dto = context.getLoginDTO();
        Dict dict = context.getDict();
        UserEntity userEntity = dict.get("user", new UserEntity());
        StpUtil.login(userEntity.getId(), IEnum.getByCode(LoginDeviceEnum.class, dto.getLoginDevice()).getText());
        StpUtil.getSession().set(GlobalConstant.ADMINISTRATOR_FLAG, userEntity.getAdministratorFlag());
        context.setVo(loginService.buildLogonVo(userEntity));
    }

    @Override
    public boolean extendParentNodeTryAttr() {
        return true;
    }
}
