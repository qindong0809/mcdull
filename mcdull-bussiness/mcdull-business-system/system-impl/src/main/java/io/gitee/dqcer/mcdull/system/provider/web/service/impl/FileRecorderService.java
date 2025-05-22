package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Dict;
import io.gitee.dqcer.mcdull.framework.oss.OssService;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.web.dao.repository.IFileRepository;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;
import org.springframework.stereotype.Service;

@Service
public class FileRecorderService implements FileRecorder {

    @Resource
    private OssService ossService;
    @Resource
    private SpringFileStorageProperties springFileStorageProperties;
    @Resource
    private IFileRepository baseRepository;

    @Override
    public boolean save(FileInfo fileInfo) {
        FileEntity file = this.getFileEntity(fileInfo);
        baseRepository.save(file);
        fileInfo.setId(file.getId().toString());
        return true;
    }


    private FileEntity getFileEntity(FileInfo upload) {
        Dict attr = upload.getAttr();
        Integer folderType = attr.get("folderType", -1);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderType(folderType);
        fileEntity.setFileName(upload.getOriginalFilename());
        fileEntity.setFileSize(Convert.toInt(upload.getSize(), -1));
        fileEntity.setFileKey(ossService.getKey(upload.getUrl()));
        fileEntity.setFileType(FileNameUtil.extName(upload.getOriginalFilename()));
        return fileEntity;
    }

    @Override
    public void update(FileInfo fileInfo) {
        FileEntity file = this.getFileEntity(fileInfo);
        baseRepository.updateById(file);
    }

    @Override
    public FileInfo getByUrl(String s) {
        FileEntity fileEntity = this.getEntityByUrl(s);
        return this.toFileInfo(fileEntity, s);
    }

    private FileEntity getEntityByUrl(String s) {
        String key = ossService.getKey(s);
        return baseRepository.getByFileKey(key);
    }

    public FileInfo toFileInfo(FileEntity detail, String url) {
         FileInfo fileInfo = new FileInfo();
         fileInfo.setId(detail.getId().toString());
         fileInfo.setUrl(url);
         fileInfo.setSize(Convert.toLong(detail.getFileSize()));
         fileInfo.setFilename(detail.getFileKey());
         fileInfo.setOriginalFilename(detail.getFileName());
         fileInfo.setCreateTime(detail.getCreatedTime());
         fileInfo.setPlatform(springFileStorageProperties.toFileStorageProperties().getDefaultPlatform());
         return fileInfo;
    }

    @Override
    public boolean delete(String s) {
        FileEntity entity = getEntityByUrl(s);
        return baseRepository.removeById(entity);
    }

    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {
        // 分片保存

    }

    @Override
    public void deleteFilePartByUploadId(String s) {
        // 删除分片
    }
}
