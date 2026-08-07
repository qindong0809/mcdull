package io.gitee.mcdull.tools.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.mcdull.tools.web.domain.DatabaseImportDTO;
import io.gitee.mcdull.tools.web.domain.ImportProgressVO;
import io.gitee.mcdull.tools.web.service.DatabaseImportService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class DatabaseImportController extends BasicController {

    @Resource
    private DatabaseImportService databaseImportService;

    /**
     * Upload a dump file and start the import. Returns as soon as the upload is stored, the import
     * itself runs in the background because it can take far longer than a request may last.
     *
     * @param file the mysql dump file
     * @param dto  target connection info and optional masking sql
     * @return task id used to poll progress
     */
    @SaIgnore
    @PostMapping("/import")
    public Result<String> importDatabase(@RequestParam(name = "file") MultipartFile file,
                                        @Valid DatabaseImportDTO dto) {
        return Result.success(databaseImportService.submitImport(file, dto));
    }

    /**
     * Poll the progress of a running or recently finished import.
     *
     * @param taskId task id returned by the import call
     * @return current progress
     */
    @SaIgnore
    @GetMapping("/import/task/{taskId}")
    public Result<ImportProgressVO> queryImportTask(@PathVariable("taskId") String taskId) {
        return Result.success(databaseImportService.getProgress(taskId));
    }
}
