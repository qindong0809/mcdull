package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.LoginFailQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.LoginLockedVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ILoginLockedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "登录锁定")
@RequestMapping
public class LoginLockedController {

    @Resource
    private ILoginLockedService loginLockedService;

    @Operation(summary = "分页查询")
    @PostMapping("/loginFail/queryPage")
    public Result<PagedVO<LoginLockedVO>> queryPage(@RequestBody @Valid LoginFailQueryDTO queryForm) {
        return Result.success(loginLockedService.queryPage(queryForm));
    }

    @Operation(summary = "批量解锁")
    @PostMapping("/loginFail/unlock")
    public Result<Boolean> unlock(@RequestBody List<Integer> idList) {
        loginLockedService.unlock(idList);
        return Result.success(true);
    }

}
