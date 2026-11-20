package io.gitee.dqcer.mcdull.demo.provider.web.controller;

import io.gitee.dqcer.mcdull.demo.facade.constant.DemoConstant;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskCreateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskQueryDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.dto.DemoTaskUpdateDTO;
import io.gitee.dqcer.mcdull.demo.facade.model.vo.DemoTaskVO;
import io.gitee.dqcer.mcdull.demo.provider.web.service.IDemoTaskService;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示任务 REST 接口。
 * <p>
 * 为便于本地学习，demo 路径在配置中放到了免鉴权清单；接入真实业务时请补充 Sa-Token 权限注解。
 */
@RestController
@RequestMapping(DemoConstant.TASK_PATH)
public class DemoTaskController extends BasicController {

    @Resource
    private IDemoTaskService demoTaskService;

    @Operation(summary = "分页查询演示任务")
    @PostMapping("/query")
    public Result<PagedVO<DemoTaskVO>> queryPage(@RequestBody @Valid DemoTaskQueryDTO dto) {
        return Result.success(demoTaskService.queryPage(dto));
    }

    @Operation(summary = "查询演示任务详情")
    @GetMapping("/{id}")
    public Result<DemoTaskVO> getById(@PathVariable Integer id) {
        return Result.success(demoTaskService.getById(id));
    }

    @Operation(summary = "新建演示任务")
    @PostMapping
    public Result<Integer> create(@RequestBody @Valid DemoTaskCreateDTO dto) {
        return Result.success(demoTaskService.create(dto));
    }

    @Operation(summary = "修改演示任务")
    @PutMapping
    public Result<Boolean> update(@RequestBody @Valid DemoTaskUpdateDTO dto) {
        demoTaskService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除演示任务")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Integer id) {
        demoTaskService.delete(id);
        return Result.success(true);
    }
}
