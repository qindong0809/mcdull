package io.gitee.dqcer.mcdull.framework.oss;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.Downloader;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * OSS 服务实现
 *
 * @author dqcer
 * @since 2025/05/22
 */
@Service
public class OssServiceImpl implements OssService{
    public static final String FOLDER_TYPE = "folderType";

    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private SpringFileStorageProperties springFileStorageProperties;

    @Override
    public FileInfo upload(MultipartFile multipartFile, Integer folderType, String path) {
        UploadPretreatment pretreatment = fileStorageService.of(multipartFile);
        pretreatment.setPath(path);
        FileInfo upload = pretreatment.upload();
        upload.setAttr(Dict.create().set(FOLDER_TYPE, folderType));
        return upload;
    }

    @Override
    public Downloader download(String url) {
        return fileStorageService.download(url);
    }

    @Override
    public Downloader downloadByKey(String key) {
        return this.download(this.convertUrl(key));
    }

    @Override
    public void delete(String url) {
        fileStorageService.delete(url);
    }

    @Override
    public void deleteByKey(String key) {
        this.delete(this.convertUrl(key));
    }

    @Override
    public String getUrl(String key) {
        return this.convertUrl(key);
    }

    @Override
    public String getKey(String url) {
        return StrUtil.subAfter(url, this.getDomain(), true);
    }

    private String convertUrl(String url) {
        return this.getDomain() + url;
    }

    private String getDomain() {
        List<? extends FileStorageProperties.LocalPlusConfig> localPlusList = springFileStorageProperties.getLocalPlus();
        if (CollUtil.isNotEmpty(localPlusList)) {
            FileStorageProperties.LocalPlusConfig localPlusConfig = localPlusList.get(0);
            return localPlusConfig.getDomain();
        }
        throw new RuntimeException("Local storage is not configured");
    }


}
