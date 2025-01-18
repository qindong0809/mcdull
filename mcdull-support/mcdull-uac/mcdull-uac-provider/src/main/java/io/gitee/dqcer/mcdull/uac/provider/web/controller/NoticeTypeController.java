package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.NoticeTypeVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.INoticeTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "通知公告类型")
@RequestMapping
public class NoticeTypeController {

    @Resource
    private INoticeTypeService noticeTypeService;
    @Operation(summary = "获取全部")
    @GetMapping("/oa/noticeType/getAll")
    public Result<List<NoticeTypeVO>> getAll() {
        return Result.success(noticeTypeService.getAll());
    }

    @Operation(summary = "添加")
    @GetMapping("/oa/noticeType/add/{name}")
    public Result<Boolean> add(@PathVariable(value = "name") String name) {
        noticeTypeService.add(name);
        return Result.success(true);
    }

    @Operation(summary = "修改")
    @GetMapping("/oa/noticeType/update/{id}/{name}")
    public Result<Boolean> update(@PathVariable(value = "id") Integer id, @PathVariable(value = "name") String name) {
        noticeTypeService.update(id, name);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @GetMapping("/oa/noticeType/delete/{id}")
    public Result<Boolean> deleteNoticeType(@PathVariable(value = "id") Integer id) {
        noticeTypeService.delete(id);
        return Result.success(true);
    }

}
