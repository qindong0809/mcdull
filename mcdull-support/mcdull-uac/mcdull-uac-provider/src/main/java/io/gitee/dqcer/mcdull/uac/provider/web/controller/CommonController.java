package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.gitee.dqcer.mcdull.framework.oss.component.OssFactory;
import io.gitee.dqcer.mcdull.framework.oss.factory.OssClient;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用请求处理
 *
 * @author dqcer
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Resource
    private OssFactory ossFactory;


    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {

    }


    @Operation(summary = "通用上传请求", description = "单个文件")
    @PostMapping("/upload")
    @SaIgnore
    public ResultWrapper uploadFile(MultipartFile file) throws Exception {
        OssClient ossClient = ossFactory.getInstance();
        // TODO: 2023/12/29
        ossClient.upload(file.getBytes(), file.getName(), "demo");
        return ResultWrapper.success();
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public ResultWrapper uploadFiles(List<MultipartFile> files) {
        return ResultWrapper.success();
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletResponse response) {
    }
}
