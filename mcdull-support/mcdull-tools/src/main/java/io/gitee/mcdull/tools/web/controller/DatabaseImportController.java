package io.gitee.mcdull.tools.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.lang.Pair;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.mcdull.tools.web.domain.DatabaseImportDTO;
import io.gitee.mcdull.tools.web.service.DatabaseImportService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class DatabaseImportController extends BasicController {

    @Resource
    private DatabaseImportService databaseImportService;


    @SaIgnore
    @PostMapping("/import")
    public Result<Pair<String, String>> importDatabase(@RequestParam(name = "file") MultipartFile file,
                                                      @Valid DatabaseImportDTO dto) {
       return Result.success(super.locker(file.getOriginalFilename(), () -> databaseImportService.dataImportAndMasker(file, dto)));
    }
}
