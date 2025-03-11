package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.EmailSendHistoryQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.EmailSendHistoryVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IEmailSendHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电子邮件发送历史记录控制器
 *
 * @author dqcer
 * @since 2024/11/19
 */
@RestController
@Tag(name = "Email发送记录")
@RequestMapping
public class EmailSendHistoryController extends BasicController {

    @Resource
    private IEmailSendHistoryService emailSendHistoryService;

    @Operation(summary = "分页查询")
    @PostMapping("/system/emails-send/query")
    public Result<PagedVO<EmailSendHistoryVO>> query(@RequestBody @Valid EmailSendHistoryQueryDTO queryDTO) {
        return Result.success(emailSendHistoryService.queryPage(queryDTO));
    }

    @Operation(summary = "导出数据")
    @SaCheckPermission("system:emails_send:export")
    @PostMapping(value = "/system/emails-send/record-export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void exportData(@RequestBody @Valid EmailSendHistoryQueryDTO dto) {
        super.locker(null, 1000, () -> emailSendHistoryService.exportData(dto));
    }

}
