package io.gitee.dqcer.mcdull.admin.web.controller.monitor;


import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 缓存控制器
 *
 * @author dqcer
 * @since 2023/03/13
 */
@RestController
public class CacheController {

    @Resource
    private RedissonClient redissonClient;

    @GetMapping("list")
    public Result demo() {
        return Result.ok();
    }
}
