package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.util.ServletUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
* 系统配置 控制器
*
* @author dqcer
* @since 2024-04-29
*/
@RestController
@Tag(name = "File API")
@RequestMapping
public class FileController {

    @Resource
    private IFileService fileService;

    @Operation(summary = "Query Page")
    @PostMapping("/file/queryPage")
    @SaCheckPermission("support:file:query")
    public Result<PagedVO<FileVO>> queryPage(@RequestBody @Valid FileQueryDTO dto) {
        return Result.success(fileService.queryPage(dto));
    }


    @Operation(summary = "Upload File")
    @PostMapping("/file/upload")
    public Result<FileUploadVO> upload(@RequestParam MultipartFile file, @RequestParam Integer folder) {
        return Result.success(fileService.fileUpload(file, folder));
    }

    @Operation(summary = "File Url")
    @GetMapping("/file/getFileUrl")
    public Result<String> getUrl(@RequestParam String fileKey) {
        return Result.success(fileService.getFileUrl(fileKey));
    }

    @Operation(summary = "Download File")
    @GetMapping("/file/downLoad")
    public void downLoad(@RequestParam String fileKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userAgent = null;
        FileDownloadVO fileDownloadVO = fileService.getDownloadFile(fileKey, userAgent);
        // 设置下载消息头
        ServletUtil.setDownloadFileHeader(response, fileDownloadVO.getMetadata().getFileName(), fileDownloadVO.getMetadata().getFileSize());

        // 下载
        response.getOutputStream().write(fileDownloadVO.getData());
    }
}
