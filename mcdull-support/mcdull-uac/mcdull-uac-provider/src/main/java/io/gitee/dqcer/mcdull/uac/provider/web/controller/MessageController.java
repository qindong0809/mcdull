package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.MessageQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.MessageVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@RestController
@Tag(name = "消息")
public class MessageController extends BasicController {

    @Resource
    private IMessageService messageService;

    @Operation(summary = "查询我的消息")
    @PostMapping("/message/queryMyMessage")
    public Result<PagedVO<MessageVO>> query(@RequestBody @Valid MessageQueryDTO queryForm) {
        queryForm.setReceiverUserId(UserContextHolder.userId());
        return Result.success(messageService.query(queryForm));
    }

    @Operation(summary = "未读消息数量")
    @GetMapping("/message/getUnreadCount")
    public Result<Integer> getUnreadCount() {
        Integer userId = UserContextHolder.userId();
        String key = "unreadCount:" + userId;
        return Result.success(super.locker(key, () -> messageService.getUnreadCount(userId)));
    }

    @Operation(summary = "更新已读")
    @GetMapping("/message/read/{messageId}")
    public Result<Boolean> updateReadFlag(@PathVariable(value = "messageId") Integer messageId) {
        String key = "update_read:" + messageId;
        return Result.success(super.locker(key, () -> messageService.updateReadFlag(messageId,  UserContextHolder.userId())));
    }

}