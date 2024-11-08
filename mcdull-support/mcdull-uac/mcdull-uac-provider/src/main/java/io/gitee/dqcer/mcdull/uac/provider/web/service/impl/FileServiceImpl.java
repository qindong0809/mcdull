package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.enums.IEnum;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.FileConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileMetadataVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileStorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl
        extends BasicServiceImpl<IFileRepository> implements IFileService {

    @Resource
    private IFileStorageService fileStorageService;

    @Resource
    private IUserManager userManager;

    @Override
    public PagedVO<FileVO> queryPage(FileQueryDTO dto) {
        String creatorName = dto.getCreatorName();
        List<Integer> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(creatorName)) {
            List<UserEntity> entityList = userManager.getLike(creatorName);
            userIdList = entityList.stream().map(IdEntity::getId).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(userIdList)) {
            return PageUtil.empty(dto);
        }
        Page<FileEntity> entityPage = baseRepository.selectPage(dto, userIdList);
        List<FileVO> voList = new ArrayList<>();
        List<FileEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Integer> userIdSet = records.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            Map<Integer, String> userMap = userManager.getNameMap(new ArrayList<>(userIdSet));
            for (FileEntity entity : records) {
                FileVO fileVO = FileConvert.convertToEntity(entity);
                fileVO.setFileUrl(fileStorageService.getFileUrl(entity.getFileKey()));
                fileVO.setCreatorName(userMap.get(entity.getCreatedBy()));
                voList.add(fileVO);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    @Override
    public FileUploadVO fileUpload(MultipartFile file, Integer folderType) {
        FileFolderTypeEnum folderTypeEnum = IEnum.getByCode(FileFolderTypeEnum.class, folderType);
        if (null == folderTypeEnum) {
            throw new BusinessException("上传文件类型错误");
        }

        if (null == file || file.getSize() == 0) {
            throw new BusinessException("上传文件不能为空");
        }
        long size = file.getSize();

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
        List<String> fileUrlList = new ArrayList<>(fileKeyArray.size());
        for (String fileKey : fileKeyArray) {
            String fileUrl = fileStorageService.getFileUrl(fileKey);
            fileUrlList.add(fileUrl);
        }
        return StrUtil.join(",", fileUrlList);
    }

    @Override
    public FileDownloadVO getDownloadFile(String fileKey, String userAgent) {
        FileEntity fileEntity = baseRepository.getByFileKey(fileKey);
        if (ObjectUtil.isNull(fileEntity)) {
           this.throwDataExistException(fileKey);
        }

        // 根据文件服务类 获取对应文件服务 查询 url
        FileDownloadVO download = fileStorageService.download(fileKey);
        FileMetadataVO metadata = download.getMetadata();
        metadata.setFileName(fileEntity.getFileName());
        return download;
    }

    @Override
    public List<FileVO> getFileList(List<String> fileKeyList) {
        if (fileKeyList.isEmpty()) {
            return Collections.emptyList();
        }
        List<FileVO> list = new ArrayList<>();
        for (String fileKey : fileKeyList) {
            FileEntity entity = baseRepository.getByFileKey(fileKey);
            if (ObjectUtil.isNull(entity)) {
                continue;
            }
            FileVO fileVO = FileConvert.convertToEntity(entity);
            String fileUrl = this.getFileUrl(fileKey);
            fileVO.setFileUrl(fileUrl);
            list.add(fileVO);
        }
        return list;
    }
}
