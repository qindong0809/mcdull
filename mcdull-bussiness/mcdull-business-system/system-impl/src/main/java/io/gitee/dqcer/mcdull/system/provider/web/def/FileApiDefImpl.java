package io.gitee.dqcer.mcdull.system.provider.web.def;

import io.gitee.dqcer.mcdull.system.facade.service.def.FileApiDef;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件能力本地实现
 */
@Primary
@Service
public class FileApiDefImpl implements FileApiDef {

    @Resource
    private IFileService fileService;

    @Override
    public void batchFileUpload(List<MultipartFile> fileList, Integer bizId, Class<?> clazz, List<Integer> deleteFileIdList) {
        fileService.batchFileUpload(fileList, bizId, clazz, deleteFileIdList);
    }

    @Override
    public void remove(Integer bizId, Class<?> clazz) {
        fileService.remove(bizId, clazz);
    }
}
