package com.dqcer.mcdull.admin.web.controller;

import com.dqcer.framework.base.annotation.UnAuthorize;
import com.dqcer.framework.base.storage.UnifySession;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @UnAuthorize
    @GetMapping("/")
    public Result<String> helloWord() {
        return Result.ok("hello word ");
    }

    @GetMapping("/404")
    public Result<?> unAuth_404() {
        return Result.ok();
    }

    @GetMapping("/user")
    public Result<UnifySession> getCurrentUserInfo() {
        return Result.ok(UserContextHolder.getSession());
    }
}
