package io.gitee.mcdull.tools.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.mcdull.tools.component.LogTailPublisher;
import io.gitee.mcdull.tools.web.domain.LogFileVO;
import io.gitee.mcdull.tools.web.domain.LogPageVO;
import io.gitee.mcdull.tools.web.domain.LogTargetVO;
import io.gitee.mcdull.tools.web.service.LogFileService;
import io.gitee.mcdull.tools.web.service.source.LogSourceRegistry;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * Log viewing: live tail over SSE and history paging over byte cursors, both filterable by keyword.
 * <p>
 * Supports multiple targets (local and remote). The UI calls /log/targets first to populate the
 * selector, then all other endpoints carry the selected target id.
 *
 * @author dqcer
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class LogController extends BasicController {

    @Resource
    private LogFileService logFileService;

    @Resource
    private LogTailPublisher logTailPublisher;

    @Resource
    private LogSourceRegistry logSourceRegistry;

    /**
     * Available targets for the UI selector.
     *
     * @return all configured targets
     */
    @SaIgnore
    @GetMapping("/targets")
    public Result<List<LogTargetVO>> targets() {
        return Result.success(logSourceRegistry.listTargets());
    }

    /**
     * Log files for a target.
     *
     * @param target target id, blank uses the default
     * @return files inside the target's directory, newest first
     */
    @SaIgnore
    @GetMapping("/files")
    public Result<List<LogFileVO>> listFiles(
            @RequestParam(name = "target", required = false) String target) {
        return Result.success(logFileService.listFiles(target));
    }

    /**
     * Reads a page of history backwards from {@code cursor}.
     *
     * @param target  target id
     * @param file    file name, blank means the active file
     * @param cursor  exclusive end byte offset, null starts at the tail
     * @param limit   maximum lines to return
     * @param keyword case insensitive substring filter
     * @return one page of lines plus the cursor for the page in front of it
     */
    @SaIgnore
    @GetMapping("/history")
    public Result<LogPageVO> history(
            @RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "file", required = false) String file,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "keyword", required = false) String keyword) {
        return Result.success(logFileService.readBackward(target, file, cursor, limit, keyword));
    }

    /**
     * Live tail of a target's active file.
     *
     * @param target  target id
     * @param keyword case insensitive substring filter, blank streams everything
     * @return server sent event stream of log lines
     */
    @SaIgnore
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestParam(name = "target", required = false) String target,
            @RequestParam(name = "keyword", required = false) String keyword) {
        return logTailPublisher.subscribe(target, keyword);
    }
}
