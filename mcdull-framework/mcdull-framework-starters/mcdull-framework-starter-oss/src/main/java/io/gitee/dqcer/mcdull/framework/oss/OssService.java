package io.gitee.dqcer.mcdull.framework.oss;

import org.dromara.x.file.storage.core.Downloader;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    FileInfo upload(MultipartFile multipartFile, Integer folderType, String path);

    Downloader download(String url);

    Downloader downloadByKey(String key);

    void delete(String url);

    void deleteByKey(String url);

    String getUrl(String key);

    String getKey(String url);
}
