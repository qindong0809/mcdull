package io.gitee.dqcer.mcdull.system.provider.web.manager;

import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;

/**
 * 文件记录持久化契约。
 *
 * <p>供 x-file-storage 的 FileRecorder 适配层使用，避免回调实现依赖高层 IFileService，
 * 从而在 FileStorageService 构造期形成循环依赖。
 */
public interface IFileRecordManager {

    boolean save(FileEntity file);

    boolean updateById(FileEntity file);

    boolean removeById(FileEntity file);

    FileEntity getByFileKey(String fileKey);
}
