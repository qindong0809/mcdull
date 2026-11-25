package io.gitee.dqcer.mcdull.system.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import io.gitee.dqcer.mcdull.framework.base.constants.I18nConstants;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IFileRecordManager;
import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.recorder.FileRecorder;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.dromara.x.file.storage.spring.SpringFileStorageProperties;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * x-file-storage 的文件记录适配器。
 *
 * <p>只依赖配置对象与文件记录 Manager，不依赖 OssService / IFileService，
 * 避免 FileStorageService 构造期与存储门面形成循环依赖。
 */
@Service
public class FileRecorderService implements FileRecorder {

    @Resource
    private SpringFileStorageProperties springFileStorageProperties;
    @Resource
    private IFileRecordManager fileRecordManager;

    @Override
    public boolean save(FileInfo fileInfo) {
        FileEntity file = this.getFileEntity(fileInfo);
        fileRecordManager.save(file);
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
        fileEntity.setFileKey(this.getKey(upload.getUrl()));
        fileEntity.setFileType(FileNameUtil.extName(upload.getOriginalFilename()));
        return fileEntity;
    }

    @Override
    public void update(FileInfo fileInfo) {
        FileEntity file = this.getFileEntity(fileInfo);
        fileRecordManager.updateById(file);
    }

    @Override
    public FileInfo getByUrl(String s) {
        FileEntity fileEntity = this.getEntityByUrl(s);
        return this.toFileInfo(fileEntity, s);
    }

    private FileEntity getEntityByUrl(String s) {
        return fileRecordManager.getByFileKey(this.getKey(s));
    }

    private String getKey(String url) {
        return StrUtil.subAfter(url, this.getDomain(), true);
    }

    private String getDomain() {
        List<? extends FileStorageProperties.LocalPlusConfig> localPlusList = springFileStorageProperties.getLocalPlus();
        if (CollUtil.isNotEmpty(localPlusList)) {
            return localPlusList.get(0).getDomain();
        }
        throw new BusinessException(I18nConstants.SYSTEM_ERROR);
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
        return fileRecordManager.removeById(entity);
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
