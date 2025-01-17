package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CacheDeleteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.CacheQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ICacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.Collection;


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

    @Operation(summary = "缓存列表")
    @PostMapping("/cache/names")
    @SaCheckPermission("support:cache:list")
    public Result<PagedVO<KeyValueVO<String, String>>> cacheNames(@RequestBody @Valid CacheQueryDTO dto) {
        return Result.success(cacheService.cacheNames(dto));
    }

    @Operation(summary = "移除缓存")
    @PostMapping("/cache/remove")
    @SaCheckPermission("support:cache:delete")
    public Result<Boolean> removeCache(@RequestBody @Valid CacheDeleteDTO dto) {
        final String key = StrUtil.format("{}:{}:{}", "cache", "remove", dto.getKey());
        return Result.success(super.locker(key,
                () -> cacheService.removeCache(dto.getCaffeineCacheFlag(), dto.getKey())));
    }

    @Operation(summary = "对应value")
    @PostMapping("/cache/key/value")
    @SaCheckPermission("support:cache:value")
    public Result<JSON> cacheKeys(@RequestBody @Valid CacheDeleteDTO dto) {
        Object o = cacheService.cacheKey(dto.getCaffeineCacheFlag(), dto.getKey());
        if (o instanceof Collection) {
            return Result.success(JSONUtil.parseArray(o));
        }

        JSON json = null;
        if (o instanceof String) {
            String str = (String) o;
            if (JSONUtil.isTypeJSONArray(str)) {
                json = JSONUtil.parseArray(str);
            } else if (JSONUtil.isTypeJSONObject(str)) {
                json = JSONUtil.parseObj(str, new JSONConfig());
            } else {
                json = JSONUtil.parseArray(ListUtil.of(o));
            }
            return Result.success(json);
        }
        return Result.success(JSONUtil.parseObj(o));
    }
}
