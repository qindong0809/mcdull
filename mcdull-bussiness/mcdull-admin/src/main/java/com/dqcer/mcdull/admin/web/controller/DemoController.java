package com.dqcer.mcdull.admin.web.controller;

import com.dqcer.framework.base.annotation.UnAuthorize;
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
}
