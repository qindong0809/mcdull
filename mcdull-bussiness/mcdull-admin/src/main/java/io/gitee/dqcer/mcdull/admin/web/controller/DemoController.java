package io.gitee.dqcer.mcdull.admin.web.controller;

import io.gitee.dqcer.mcdull.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示控制器
 *
 * @author dqcer
 * @date 2023/01/18 22:01:33
 */
@RestController
public class DemoController {

    @UnAuthorize
    @GetMapping("/")
    public Result<String> helloWord() {
        return Result.ok("hello word ");
    }

    @GetMapping("/error")
    public Result<?> unAuth404() {
        Integer.valueOf("dfd");
        return Result.ok();
    }

    @GetMapping("/user")
    public Result<UnifySession> getCurrentUserInfo() {
        return Result.ok(UserContextHolder.getSession());
    }
}
