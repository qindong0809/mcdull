package io.gitee.dqcer.mcdull.focus.web.controller;

import cn.hutool.core.convert.Convert;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskCreateDTO;
import io.gitee.dqcer.mcdull.focus.model.dto.TaskUpdateDTO;
import io.gitee.dqcer.mcdull.focus.model.entity.AppTaskEntity;
import io.gitee.dqcer.mcdull.focus.web.service.ITaskService;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TaskController extends BasicController {

    @Resource
    private ITaskService taskService;

    @Operation(summary = "task list")
    @PostMapping(GlobalConstant.APP_PATH + "/task/list")
    public Result<List<AppTaskEntity>> list(@RequestBody Map<String, Object> param) {
        Boolean completed = Convert.toBool(param.get("completed"), null);
        return Result.success(taskService.listByUser(UserContextHolder.userId(), completed));
    }

    @Operation(summary = "task create")
    @PostMapping(GlobalConstant.APP_PATH + "/task/create")
    public Result<Map<String, Object>> create(@RequestBody @Valid TaskCreateDTO dto) {
        List<AppTaskEntity> created = taskService.createBatch(dto, UserContextHolder.userId());
        return Result.success(Map.of(
            "ids", created.stream().map(AppTaskEntity::getId).toList(),
            "dueDates", created.stream().map(AppTaskEntity::getDueDate).toList()
        ));
    }

    // /task/update — 更新任务
    @Operation(summary = "task update")
    @PostMapping(GlobalConstant.APP_PATH + "/task/update")
    public Result<Map<String, Object>> update(@RequestBody @Valid TaskUpdateDTO dto) {
        taskService.update(dto);
        return Result.success(Map.of("id", dto.getId(), "dueDate", dto.getDueDate()));
    }

    @Operation(summary = "task delete")
    @PostMapping(GlobalConstant.APP_PATH + "/task/delete")
    public Result<Integer> delete(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        taskService.deleteById(id);
        return Result.success(id);
    }

    @Operation(summary = "task toggle")
    @PostMapping(GlobalConstant.APP_PATH + "/task/toggle")
    public Result<Integer> toggle(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        taskService.toggleCompleted(id);
        return Result.success(id);
    }

}
