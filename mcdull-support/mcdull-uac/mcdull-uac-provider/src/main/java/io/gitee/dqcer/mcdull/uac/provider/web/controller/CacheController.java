package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * cache
 *
 * @author dqcer
 */
@Tag(name = "缓存信息")
@RestController
public class CacheController extends BasicController {

    @Resource
    private ICacheService cacheService;

    @Operation(summary = "获取所有缓存")
    @GetMapping("/cache/names")
    @SaCheckPermission("support:cache:keys")
    public Result<List<String>> cacheNames() {
        return Result.success(cacheService.cacheNames());
    }

    @Operation(summary = "移除某个缓存")
    @GetMapping("/cache/remove/{cacheName}")
    @SaCheckPermission("support:cache:delete")
    public Result<Boolean> removeCache(@PathVariable String cacheName) {
        cacheService.removeCache(cacheName);
        return Result.success(true);
    }

    @Operation(summary = "获取某个缓存的所有key")
    @GetMapping("/cache/keys/{cacheName}")
    @SaCheckPermission("support:cache:keys")
    public Result<List<String>> cacheKeys(@PathVariable String cacheName) {
        return Result.success(cacheService.cacheKey(cacheName));
    }
}
