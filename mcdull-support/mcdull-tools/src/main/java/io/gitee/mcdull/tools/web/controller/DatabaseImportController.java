package io.gitee.mcdull.tools.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.mcdull.tools.web.domain.DatabaseImportDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DatabaseImportController {

    @SaIgnore
    @PostMapping("/import")
    public String importDatabase(@RequestParam(name = "file", required = false)MultipartFile file, @Valid DatabaseImportDTO dto) {
        System.out.println(dto);
        return "success";
    }
}
