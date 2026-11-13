package io.gitee.dqcer.mcdull.system.provider.flow.login;

import cn.hutool.core.lang.Dict;
import io.gitee.dqcer.mcdull.framework.flow.node.Context;
import io.gitee.dqcer.mcdull.system.provider.model.dto.LoginDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.LogonVO;
import lombok.Data;

@Data
public class LoginContext implements Context {

    private String id;

    private Dict dict;

    private LoginDTO loginDTO;

    private LogonVO vo;
}
