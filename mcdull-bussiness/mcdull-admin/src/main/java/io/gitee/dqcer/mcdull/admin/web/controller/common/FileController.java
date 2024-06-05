package io.gitee.dqcer.mcdull.admin.web.controller.common;

import io.gitee.dqcer.mcdull.admin.web.service.common.IFileService;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * file controller
 *
 * @author dqcer
 * @since 2023/03/11
 */
@RestController
@RequestMapping("/common")
public class FileController extends BasicController {


    @Resource
    private IFileService fileService;


    @PostMapping("/upload")
    public Result<?> uploadFile(@NotNull MultipartFile file) {
        return fileService.save(file);
    }

}
