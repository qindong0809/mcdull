package io.gitee.dqcer.mcdull.system.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FeedbackAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FeedbackQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FeedbackVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 意见反馈
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "意见反馈")
@RequestMapping
public class FeedbackController {

    @Resource
    private IFeedbackService feedbackService;

    @Operation(summary = "分页查询")
    @PostMapping("/feedback/query")
    public Result<PagedVO<FeedbackVO>> query(@RequestBody @Valid FeedbackQueryDTO dto) {
        return Result.success(feedbackService.query(dto));
    }

    @Operation(summary = "新增")
    @PostMapping("/feedback/add")
    public Result<Boolean> add(@RequestBody @Valid FeedbackAddDTO dto) {
        feedbackService.add(dto);
        return Result.success(true);
    }
}
