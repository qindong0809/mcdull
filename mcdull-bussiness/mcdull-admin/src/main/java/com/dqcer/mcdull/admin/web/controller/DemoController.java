package io.gitee.dqcer.admin.web.controller;

import io.gitee.dqcer.framework.base.annotation.UnAuthorize;
import io.gitee.dqcer.framework.base.storage.UnifySession;
import io.gitee.dqcer.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @UnAuthorize
    @GetMapping("/")
    public Result<String> helloWord() {
        return Result.ok("hello word ");
    }

    @GetMapping("/error")
    public Result<?> unAuth_404() {
        Integer.valueOf("dfd");
        return Result.ok();
    }

    @GetMapping("/user")
    public Result<UnifySession> getCurrentUserInfo() {
        return Result.ok(UserContextHolder.getSession());
    }
}
