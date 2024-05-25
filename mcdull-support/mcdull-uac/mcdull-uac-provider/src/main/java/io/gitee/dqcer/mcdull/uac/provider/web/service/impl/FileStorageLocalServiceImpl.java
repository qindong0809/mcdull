package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.help.LogHelp;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileMetadataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageLocalServiceImpl implements IFileStorageService {

    public static final String UPLOAD_MAPPING = "/upload";

    @Value("${file.storage.local.upload-path:/home/upload/}")
    private String uploadPath;

    private String urlPrefix;

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Value("${server.port}")
    private String port;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    @PostConstruct
    public void initUrlPrefix() {
        String localhostIp = NetUtil.getLocalhostStr();
        String finalContextPath = contextPath.startsWith("/") ? contextPath : "/" + contextPath;
        if (finalContextPath.endsWith("/")) {
            finalContextPath = finalContextPath.substring(0, finalContextPath.length() - 1);
        }
        urlPrefix = "http://" + "mcdull.io" + ":" + port + finalContextPath + UPLOAD_MAPPING;
        urlPrefix = urlPrefix.endsWith("/") ? urlPrefix : urlPrefix + "/";
    }


    @Override
    public FileUploadVO upload(MultipartFile multipartFile, String path) {
        if (null == multipartFile) {
            throw new BusinessException("上传文件不能为空");
        }
        String filePath = uploadPath + path;
        File directory = new File(filePath);
        if (!directory.exists()) {
            // 目录不存在，新建
            directory.mkdirs();
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        FileUploadVO fileUploadVO = new FileUploadVO();
        //原文件名
        String originalFileName = multipartFile.getOriginalFilename();
        //新文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_FORMATTER);
        String newFileName = uuid + "_" + time;
//        String fileType = FilenameUtils.getExtension(originalFileName);
        String fileType = FileNameUtil.extName(originalFileName);
        if (StrUtil.isNotEmpty(fileType)) {
            newFileName = newFileName + "." + fileType;
        }
        //生成文件key
        String fileKey = path + newFileName;
        //创建文件
        File fileTemp = new File(new File(filePath + newFileName).getAbsolutePath());
        try {
            multipartFile.transferTo(fileTemp);
            fileUploadVO.setFileUrl(this.generateFileUrl(fileKey));
            fileUploadVO.setFileName(newFileName);
            fileUploadVO.setFileKey(fileKey);
//            fileUploadVO.setFileSize(multipartFile.getSize());
//            fileUploadVO.setFileType(FilenameUtils.getExtension(originalFileName));
            fileUploadVO.setFileType(FileNameUtil.extName(originalFileName));
        } catch (IOException e) {
            if (fileTemp.exists() && fileTemp.isFile()) {
                fileTemp.delete();
            }
            LogHelp.error(log, "上传失败", e);
            throw new BusinessException("上传失败");
        }
        return fileUploadVO;
    }

    public String generateFileUrl(String filePath) {
        return urlPrefix + filePath;
    }

    @Override
    public String getFileUrl(String fileKey) {
        return this.generateFileUrl(fileKey);
    }

    @Override
    public FileDownloadVO download(String fileKey) {
        String filePath = uploadPath + fileKey;
        File localFile = new File(filePath);
        InputStream in = null;
        try {
            in = Files.newInputStream(localFile.toPath());
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(in);
            FileDownloadVO fileDownloadVO = new FileDownloadVO();
            fileDownloadVO.setData(buffer);

            FileMetadataVO fileMetadataDTO = new FileMetadataVO();
            fileMetadataDTO.setFileName(localFile.getName());
            fileMetadataDTO.setFileSize(Convert.toInt(localFile.length()));
//            fileMetadataDTO.setFileFormat(FilenameUtils.getExtension(localFile.getName()));
            fileMetadataDTO.setFileFormat(FileNameUtil.extName(localFile.getName()));
            fileDownloadVO.setMetadata(fileMetadataDTO);

            return fileDownloadVO;
        } catch (IOException e) {
            LogHelp.error(log, "文件下载-发生异常：", e);
            throw new BusinessException("文件下载失败");
        } finally {
            try {
                // 关闭输入流
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                LogHelp.error(log, "文件下载-发生异常：", e);
            }
        }
    }

    @Override
    public void delete(String fileKey) {
        String filePath = uploadPath + fileKey;
        File localFile = new File(filePath);
//            FileUtils.forceDelete(localFile);
        boolean del = FileUtil.del(localFile);
        if (!del) {
            LogHelp.error(log, "删除本地文件失败：{}", filePath);
        }

    }
}
