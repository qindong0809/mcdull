package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.*;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictKeyVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DictValueVO;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IDictTypeManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictKeyService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDictValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;


/**
 * Dict Info
 *
 * @author dqcer
 * @since 2022/09/05
 */
@Tag(name = "数据字典")
@RestController
public class DictController extends BasicController {

    @Resource
    private IDictKeyService dictKeyService;
    @Resource
    private IDictValueService dictValueService;
    @Resource
    private IDictTypeManager dictTypeManager;


    @Operation(summary = "查询全部")
    @GetMapping("/dict/key/queryAll")
    public Result<List<DictKeyVO>> getKeyAll() {
        return Result.success(dictKeyService.queryAll());
    }

    @Operation(summary = "分页列表")
    @PostMapping("/dict/key/query")
    public Result<PagedVO<DictKeyVO>> getKeyList(@Valid @RequestBody DictKeyQueryDTO queryDTO) {
        return Result.success(dictKeyService.queryPage(queryDTO));
    }

    @Operation(summary = "导出数据")
    @SaCheckEL("stp.checkPermission('support:dict:export')")
    @PostMapping(value = "/dict/key/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid DictKeyQueryDTO dto) {
        super.locker(null, () -> dictKeyService.exportData(dto));
    }

    @Operation(summary = "添加字典")
    @PostMapping("/dict/key/add")
    @SaCheckEL("stp.checkPermission('support:dict:add')")
    public Result<Boolean> keyAdd(@Valid @RequestBody DictKeyAddDTO keyAddDTO) {
        dictKeyService.insert(keyAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "删除字典")
    @PostMapping("/dict/key/delete")
    @SaCheckEL("stp.checkPermission('support:dict:delete')")
    public Result<Boolean> keyDelete(@RequestBody List<Integer> keyIdList) {
        dictKeyService.delete(keyIdList);
        return Result.success(true);
    }

    @Operation(summary = "编辑字典")
    @PostMapping("/dict/key/edit")
    @SaCheckEL("stp.checkPermission('support:dict:edit')")
    public Result<Boolean> keyEdit(@Valid @RequestBody DictKeyUpdateDTO keyUpdateDTO) {
        dictKeyService.update(keyUpdateDTO);
        return Result.success(true);
    }

    @Operation(summary = "数据字典缓存-刷新- Result<")
    @GetMapping("/dict/cache/refresh")
    @SaCheckEL("stp.checkPermission('support:dict:refresh')")
    public Result<String> cacheRefresh() {
        dictTypeManager.clean();
        return Result.success();
    }

    @Operation(summary = "字典值分页类别")
    @PostMapping("/dict/value/query")
    public Result<PagedVO<DictValueVO>> getList(@Valid @RequestBody DictValueQueryDTO queryDTO) {
        return Result.success(dictValueService.getList(queryDTO));
    }

    @Operation(summary = "添加字典值")
    @PostMapping("/dict/value/add")
    public Result<Boolean> valueAdd(@Valid @RequestBody DictValueAddDTO valueAddDTO) {
        dictValueService.insert(valueAddDTO);
        return Result.success(true);
    }

    @Operation(summary = "编辑字典值")
    @PostMapping("/dict/value/edit")
    public Result<Boolean> valueEdit(@Valid @RequestBody DictValueUpdateDTO valueUpdateDTO) {
        dictValueService.update(valueUpdateDTO);
        return Result.success(true);
    }

    @Operation(summary = "删除字典值")
    @PostMapping("/dict/value/delete")
    public Result<Boolean> valueDelete(@RequestBody List<Integer> valueIdList) {
        dictValueService.delete(valueIdList);
        return Result.success(true);
    }

    @Operation(summary = "Value list(by keyCode)")
    @GetMapping("/dict/value/list/{keyCode}")
    public Result<List<DictValueVO>> valueList(@PathVariable(value = "keyCode") String keyCode) {
        List<DictValueVO> dictValueVOList = dictValueService.selectByKeyCode(keyCode);
        return Result.success(dictValueVOList);
    }
}
