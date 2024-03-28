package io.gitee.dqcer.mcdull.admin.web.service.common.impl;

import cn.hutool.core.io.FileUtil;
import io.gitee.dqcer.mcdull.admin.model.entity.common.SysFileDO;
import io.gitee.dqcer.mcdull.admin.model.enums.SysConfigKeyEnum;
import io.gitee.dqcer.mcdull.admin.web.dao.repository.common.ISysFileRepository;
import io.gitee.dqcer.mcdull.admin.web.manager.common.ISysConfigManager;
import io.gitee.dqcer.mcdull.admin.web.service.common.IFileService;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * sys dict服务
 *
 * @author dqcer
 * @since  2022/11/08
 */
@Service
public class FileServiceImpl extends BasicServiceImpl<ISysFileRepository> implements IFileService {

    @Resource
    private ISysConfigManager sysConfigManager;

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> save(MultipartFile multipartFile) {
        String valueByEnum = sysConfigManager.findValueByEnum(SysConfigKeyEnum.FILE_DIRECTORY);
        FileUtil.writeBytes(multipartFile.getBytes(), valueByEnum  +  multipartFile.getOriginalFilename());
        return Result.success(baseRepository.save(this.buildEntity(multipartFile)));
    }

    private SysFileDO buildEntity(MultipartFile multipartFile) throws IOException {
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        InputStream inputStream = multipartFile.getInputStream();
//        String md5 = SecureUtil.md5(inputStream);
        String md5 = "dsfsdf";
        SysFileDO entity = new SysFileDO();
        entity.setFileSize(size);
        entity.setFileMd5(md5);
        entity.setOriginalFileName(originalFilename);
        entity.setFileType(originalFilename);
        return entity;
    }
}
