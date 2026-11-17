package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CacheDeleteDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CacheQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ICacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;


/**
 * cache
 * @author dqcer
 * @since 2023/01/03
 */
@Tag(name = "缓存信息")
@RestController
public class CacheController extends BasicController {

    @Resource
    private ICacheService cacheService;

    @Operation(summary = "缓存列表")
    @PostMapping("/cache/names")
    @SaCheckEL("stp.checkPermission('support:cache:list')")
    public Result<PagedVO<KeyValueVO<String, String>>> cacheNames(@RequestBody @Valid CacheQueryDTO dto) {
        return Result.success(cacheService.queryPage(dto));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('support:cache:export')")
    @PostMapping(value = "/cache/cache/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid CacheQueryDTO dto) {
        super.locker(null, () -> cacheService.exportData(dto));
    }

    @Operation(summary = "移除缓存")
    @PostMapping("/cache/remove")
    @SaCheckEL("stp.checkPermission('support:cache:delete')")
    public Result<Boolean> removeCache(@RequestBody @Valid CacheDeleteDTO dto) {
        String key = dto.getKey();
        return Result.success(super.locker(key, () -> cacheService.removeCache(dto.getCaffeineCacheFlag(), key)));
    }

    @Operation(summary = "对应value")
    @PostMapping("/cache/key/value")
    @SaCheckEL("stp.checkPermission('support:cache:value')")
    public Result<Object> cacheKeys(@RequestBody @Valid CacheDeleteDTO dto) {
        Object o = cacheService.cacheKey(dto.getCaffeineCacheFlag(), dto.getKey());
        if (o instanceof Collection) {
            return Result.success(JSONArray.from(o));
        }
        Object json;
        if (o instanceof String) {
            String str = Convert.toStr(o);
            if (JSON.isValidArray(str)) {
                json = JSON.parseArray(str);
            } else if (JSON.isValidObject(str)) {
                json = JSON.parseObject(str);
            } else {
                json = JSONArray.from(ListUtil.of(o));
            }
            return Result.success(json);
        }
        if (o instanceof Map) {
            return Result.success(JSONObject.from(o));
        }
        // 标量缓存值不是 JSON 对象，原样返回
        return Result.success(o);
    }
}
