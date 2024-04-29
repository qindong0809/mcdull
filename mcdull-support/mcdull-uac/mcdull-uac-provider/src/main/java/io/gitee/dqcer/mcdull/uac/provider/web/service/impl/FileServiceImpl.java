package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.FileConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileMetadataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl extends BasicServiceImpl<IFileRepository> implements IFileService {

    @Resource
    private IFileStorageService fileStorageService;

    @Override
    public PagedVO<FileVO> queryPage(FileQueryDTO dto) {
        Page<FileEntity> entityPage = baseRepository.selectPage(dto);
        List<FileVO> voList = new ArrayList<>();
        for (FileEntity entity : entityPage.getRecords()) {
            voList.add(FileConvert.convertToEntity(entity));
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public FileUploadVO fileUpload(MultipartFile file, Integer folderType) {
        FileFolderTypeEnum folderTypeEnum = IEnum.getByCode(FileFolderTypeEnum.class, folderType);
        if (null == folderTypeEnum) {
            throw new BusinessException("上传文件类型错误");
        }

        long size = file.getSize();
        if (null == file || size == 0) {
            throw new BusinessException("上传文件不能为空");
        }

        // 校验文件名称
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new BusinessException("上传文件名称不能为空");
        }

        if (originalFilename.length() > GlobalConstant.Number.NUMBER_20 * 5) {
            throw new BusinessException("文件名称最大长度为：" + GlobalConstant.Number.NUMBER_20 * 5);
        }

        // 校验文件大小
        int finalMaxSize = 100;
        long maxSize = finalMaxSize * 1024 * 1024L;
        if (size > maxSize) {
            throw new BusinessException("上传文件最大为:" + maxSize);
        }

        // 进行上传
        FileUploadVO uploadVO = fileStorageService.upload(file, folderTypeEnum.getFolder());
        // 上传成功 保存记录数据库
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderType(folderTypeEnum.getValue());
        fileEntity.setFileName(originalFilename);
        fileEntity.setFileSize(Convert.toInt(size));
        fileEntity.setFileKey(uploadVO.getFileKey());
        fileEntity.setFileType(uploadVO.getFileType());
        baseRepository.insert(fileEntity);

        // 将fileId 返回给前端
        uploadVO.setFileId(fileEntity.getId());
        return uploadVO;
    }

    @Override
    public String getFileUrl(String fileKeyList) {
        List<String> fileKeyArray = StrUtil.split(fileKeyList, StrUtil.C_COMMA);
        List<String> fileUrlList = Lists.newArrayListWithCapacity(fileKeyArray.size());
        for (String fileKey : fileKeyArray) {
            String fileUrl = fileStorageService.getFileUrl(fileKey);
            fileUrlList.add(fileUrl);
        }
        return StrUtil.join(",", fileUrlList);
    }

    @Override
    public FileDownloadVO getDownloadFile(String fileKey, String userAgent) {
        FileEntity fileEntity = baseRepository.getByFileKey(fileKey);
        if (fileEntity == null) {
           this.throwDataExistException(fileKey);
        }

        // 根据文件服务类 获取对应文件服务 查询 url
        FileDownloadVO download = fileStorageService.download(fileKey);
        FileMetadataVO metadata = download.getMetadata();
        metadata.setFileName(fileEntity.getFileName());
        return download;
    }
}
