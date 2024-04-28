package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.*;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictKeyService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IDictValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * Dict Info
 *
 * @author dqcer
 */
@Tag(name = "Dict Info")
@RestController
public class DictController extends BasicController {

    @Resource
    private IDictKeyService dictKeyService;

    @Resource
    private IDictValueService dictValueService;


    @Operation(summary = "Query key all")
    @GetMapping("/dict/key/queryAll")
    public Result<List<DictKeyVO>> getKeyAll() {
        return Result.success(dictKeyService.queryAll());
    }

    @Operation(summary = "Query key paged list")
    @PostMapping("/dict/key/query")
    public Result<PagedVO<DictKeyVO>> getKeyList(@Valid @RequestBody DictKeyQueryDTO queryDTO) {
        return Result.success(dictKeyService.getList(queryDTO));
    }


    @Operation(summary = "Key add")
    @PostMapping("/dict/key/add")
    @SaCheckPermission("support:dict:add")
    public Result<Boolean> keyAdd(@Valid @RequestBody DictKeyAddDTO keyAddDTO) {
        dictKeyService.insert(keyAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "Key delete")
    @PostMapping("/dict/key/delete")
    @SaCheckPermission("support:dict:delete")
    public Result<Boolean> keyDelete(@RequestBody List<Long> keyIdList) {
        dictKeyService.delete(keyIdList);
        return Result.success(true);
    }

    @Operation(summary = "Key edit")
    @PostMapping("/dict/key/edit")
    @SaCheckPermission("support:dict:edit")
    public Result<Boolean> keyEdit(@Valid @RequestBody DictKeyUpdateDTO keyUpdateDTO) {
        dictKeyService.update(keyUpdateDTO);
        return Result.success(true);
    }


    @Operation(summary = "数据字典缓存-刷新- Result<")
    @GetMapping("/dict/cache/refresh")
    @SaCheckPermission("support:dict:refresh")
    public Result<String> cacheRefresh() {
        return dictCacheService.cacheRefresh();
    }

    @Operation(summary = "Query value paged list")
    @PostMapping("/dict/value/query")
    public Result<PagedVO<DictValueVO>> getList(@Valid @RequestBody DictValueQueryDTO queryDTO) {
        return Result.success(dictValueService.getList(queryDTO));
    }

    @Operation(summary = "Value add")
    @PostMapping("/dict/value/add")
    public Result<Boolean> valueAdd(@Valid @RequestBody DictValueAddDTO valueAddDTO) {
        dictValueService.insert(valueAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "数据字典Value-更新- Result<")
    @PostMapping("/dict/value/edit")
    public Result<String> valueEdit(@Valid @RequestBody DictValueUpdateDTO valueUpdateDTO) {
        return dictValueService.valueEdit(valueUpdateDTO);
    }

    @Operation(summary = "数据字典Value-删除- Result<")
    @PostMapping("/dict/value/delete")
    public Result<String> valueDelete(@RequestBody List<Long> valueIdList) {
        return dictValueService.valueDelete(valueIdList);
    }

    @Operation(summary = "数据字典-值列表- Result<")
    @GetMapping("/dict/value/list/{keyCode}")
    public Result<List<DictValueVO>> valueList(@PathVariable String keyCode) {
        List<DictValueVO> dictValueVOList = dictValueService.selectByKeyCode(keyCode);
        return Result.ok(dictValueVOList);
    }
}
