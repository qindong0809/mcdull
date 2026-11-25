package io.gitee.dqcer.mcdull.system.provider.web.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.IFileRecordManager;
import io.gitee.dqcer.mcdull.system.provider.web.mapper.FileMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 文件记录持久化实现。
 */
@Service
public class FileRecordManagerImpl implements IFileRecordManager {

    @Resource
    private FileMapper fileMapper;

    @Override
    public boolean save(FileEntity file) {
        return fileMapper.insert(file) > 0;
    }

    @Override
    public boolean updateById(FileEntity file) {
        return fileMapper.updateById(file) > 0;
    }

    @Override
    public boolean removeById(FileEntity file) {
        if (file == null || file.getId() == null) {
            return false;
        }
        return fileMapper.deleteById(file.getId()) > 0;
    }

    @Override
    public FileEntity getByFileKey(String fileKey) {
        if (StrUtil.isBlank(fileKey)) {
            return null;
        }
        return fileMapper.selectOne(Wrappers.lambdaQuery(FileEntity.class)
                .eq(FileEntity::getFileKey, fileKey));
    }
}
