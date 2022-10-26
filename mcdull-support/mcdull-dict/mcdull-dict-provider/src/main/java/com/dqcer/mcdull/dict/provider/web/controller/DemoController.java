package com.dqcer.mcdull.dict.provider.web.controller;

import com.dqcer.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping()
    public Result<String> get() {
        return Result.ok("provider");
    }
}
