package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.TableColumnUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.ITableColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 *
 * @author dqcer
 */
@Tag(name = "Table Column")
@RestController
public class TableColumnController extends BasicController {

    @Resource
    private ITableColumnService tableColumnService;

    @Operation(summary = "修改表格列")
    @PostMapping("/tableColumn/update")
    @RedisLock(key = "'lock:uac:user:' + #dto.tableId", timeout = 3)
    public Result<Boolean> updateTableColumn(@RequestBody @Valid TableColumnUpdateDTO dto) {
        tableColumnService.updateTableColumns(dto);
        return Result.success(true);
    }

    @Operation(summary = "恢复默认（删除） ")
    @GetMapping("/tableColumn/delete/{tableId}")
    @RedisLock(key = "'lock:uac:user:' + #tableId", timeout = 3)
    public Result<Boolean> deleteTableColumn(@PathVariable Integer tableId) {
        tableColumnService.deleteTableColumn(tableId);
        return Result.success(true);
    }

    @Operation(summary = "查询表格列 ")
    @GetMapping("/tableColumn/getColumns/{tableId}")
    public Result<String> getColumns(@PathVariable Integer tableId) {
        return Result.success(tableColumnService.getTableColumns(tableId));
    }

}
