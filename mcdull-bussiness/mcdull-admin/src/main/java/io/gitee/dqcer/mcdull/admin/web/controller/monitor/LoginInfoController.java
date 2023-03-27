package io.gitee.dqcer.mcdull.admin.web.controller.monitor;


import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LoginInfoVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILoginInfoService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 登录信息控制器
 *
 * @author dqcer
 * @since 2023/03/13
 */
@RequestMapping("/monitor/logininfor")
@RestController
public class LoginInfoController {

    @Resource
    private ILoginInfoService loginInfoService;

    @Transform
    @Authorized("monitor:logininfor:list")
    @GetMapping("list")
    public Result<PagedVO<LoginInfoVO>> list(LoginInfoLiteDTO dto) {
        return loginInfoService.listByPage(dto);
    }
}
