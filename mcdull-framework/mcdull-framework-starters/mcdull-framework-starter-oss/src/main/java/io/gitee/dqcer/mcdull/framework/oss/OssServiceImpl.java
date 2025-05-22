package io.gitee.dqcer.mcdull.framework.oss;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.Downloader;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
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

    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private FileStorageProperties fileStorageProperties;

    @Override
    public FileInfo upload(MultipartFile multipartFile, String path) {
        UploadPretreatment pretreatment = fileStorageService.of(multipartFile);
        pretreatment.setPath(path);
        return pretreatment.upload();
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

    private String convertUrl(String url) {
        List<? extends FileStorageProperties.LocalPlusConfig> localPlusList = fileStorageProperties.getLocalPlus();
        if (CollUtil.isNotEmpty(localPlusList)) {
            FileStorageProperties.LocalPlusConfig localPlusConfig = localPlusList.get(0);
            return localPlusConfig.getDomain() + url;
        }
        throw new RuntimeException("Local storage is not configured");
    }


}
