package io.gitee.dqcer.mcdull.uac.provider.web.service.impl;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.gitee.dqcer.mcdull.business.common.CustomMultipartFile;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.util.PageUtil;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicServiceImpl;
import io.gitee.dqcer.mcdull.uac.provider.model.convert.FileConvert;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.UserEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.enums.FileFolderTypeEnum;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.*;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileBizRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.dao.repository.IFileRepository;
import io.gitee.dqcer.mcdull.uac.provider.web.manager.IUserManager;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileBizService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFileStorageService;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IFolderService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl
        extends BasicServiceImpl<IFileRepository> implements IFileService {

    @Resource
    private IFileStorageService fileStorageService;
    @Resource
    private IUserManager userManager;
    @Resource
    private IFileBizRepository fileBizRepository;
    @Resource
    private IFolderService folderService;
    @Resource
    private IFileBizService fileBizService;

    @Override
    public PagedVO<FileVO> queryPage(FileQueryDTO dto) {
        String creatorName = dto.getCreatorName();
        List<Integer> userIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(creatorName)) {
            List<UserEntity> entityList = userManager.getLike(creatorName);
            userIdList = entityList.stream().map(IdEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(userIdList)) {
                return PageUtil.empty(dto);
            }
        }
        List<Integer> childerList = new ArrayList<>();
        Integer folderId = dto.getFolderType();
        if (ObjectUtil.isNotNull(folderId)) {
            childerList.add(folderId);
            List<FolderInfoVO> all = folderService.getAll();
            this.searchChild(all, folderId, childerList);
        }

        Page<FileEntity> entityPage = baseRepository.selectPage(dto, userIdList, childerList);
        List<FileVO> voList = new ArrayList<>();
        List<FileEntity> records = entityPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Map<Integer, String> folderMap = folderService.getMap(records.stream().map(FileEntity::getFolderType).collect(Collectors.toSet()));
            Set<Integer> userIdSet = records.stream().map(BaseEntity::getCreatedBy).collect(Collectors.toSet());
            Map<Integer, String> userMap = userManager.getMap(records);
            for (FileEntity entity : records) {
                FileVO fileVO = FileConvert.convertToEntity(entity);
                fileVO.setFileUrl(fileStorageService.getFileUrl(entity.getFileKey()));
                fileVO.setCreatorName(userMap.get(entity.getCreatedBy()));
                fileVO.setFolderTypeName(folderMap.get(entity.getFolderType()));
                voList.add(fileVO);
            }
        }
        return PageUtil.toPage(voList, entityPage);
    }

    private void searchChild(List<FolderInfoVO> all, Integer folderId, List<Integer> childerList) {
        for (FolderInfoVO folder : all) {
            if (ObjectUtil.equal(folder.getParentId(), folderId)) {
                Integer childId = folder.getId();
                childerList.add(childId);
                this.searchChild(all, childId, childerList);
            }
        }
    }

    @Override
    public FileUploadVO fileUpload(MultipartFile file, Integer folderType) {
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
        String rootToNodeName = folderService.getRootToNodeName(folderType);
        FileUploadVO uploadVO = fileStorageService.upload(file, FileFolderTypeEnum.FOLDER_PUBLIC + "/" + rootToNodeName + "/");
        // 上传成功 保存记录数据库
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFolderType(folderType);
        fileEntity.setFileName(originalFilename);
        fileEntity.setFileSize(Convert.toInt(size));
        fileEntity.setFileKey(uploadVO.getFileKey());
        fileEntity.setFileType(uploadVO.getFileType());
        baseRepository.insert(fileEntity);

        // 将fileId 返回给前端
        uploadVO.setFileId(fileEntity.getId());
        return uploadVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchFileUpload(List<MultipartFile> fileList, Integer bizId, Class<?> clazz, List<Integer> deleteFileIdList) {
        if (CollUtil.isNotEmpty(deleteFileIdList)) {
            for (Integer deletedFileId : deleteFileIdList) {
                this.remove(deletedFileId, bizId, clazz);
            }
        }
        if (CollUtil.isNotEmpty(fileList)) {
            Integer systemFolderId = folderService.getSystemFolderId();
            for (MultipartFile multipartFile : fileList) {
                FileUploadVO fileUploadVO = this.fileUpload(multipartFile, systemFolderId);
                fileBizRepository.save(ListUtil.of(fileUploadVO.getFileId()), bizId, this.getTableName(clazz));
            }
        }
    }

    @Override
    public FileUploadVO fileUpload(File file, Integer folder) {
        CustomMultipartFile multipartFile = new CustomMultipartFile(file.getName(), file.getName(), "application/zip", FileUtil.readBytes(file));
        return this.fileUpload(multipartFile, folder);
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
    public FileDownloadVO getDownloadFile(String fileKey) {
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


    @Override
    public Map<Integer, FileEntity> map(Set<Integer> fileIdSet) {
        if (CollUtil.isNotEmpty(fileIdSet)) {
            List<FileEntity> list = baseRepository.listByIds(fileIdSet);
            if (CollUtil.isNotEmpty(list)) {
                return list.stream().collect(Collectors.toMap(FileEntity::getId, Function.identity()));
            }
        }
        return Map.of();
    }

    public void remove(Integer fileId, Integer bizId, Class<?> clazz) {
        String tableName = this.getTableName(clazz);
        Map<Integer, List<Integer>> listMap = fileBizService.get(tableName);
        if (MapUtil.isNotEmpty(listMap)) {
            List<Integer> fileIdList = listMap.get(bizId);
            if (CollUtil.isNotEmpty(fileIdList)) {
                if (fileIdList.contains(fileId)) {
                    baseRepository.removeById(fileId);
                }
            }
            fileBizRepository.deleteByBizCode(fileId, bizId, tableName);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void remove(Integer bizId, Class<?> clazz) {
        String tableName = this.getTableName(clazz);
        Map<Integer, List<Integer>> listMap = fileBizService.get(tableName);
        if (MapUtil.isNotEmpty(listMap)) {
            List<Integer> fileIdList = listMap.get(bizId);
            if (CollUtil.isNotEmpty(fileIdList)) {
                baseRepository.removeByIds(fileIdList);
            }
            fileBizRepository.deleteByBizCode(bizId, tableName);
        }
    }

    @Override
    public Map<Integer, List<FileEntity>> get(List<Integer> bizIdList, Class<?> aClass) {
        String tableName = this.getTableName(aClass);
        Map<Integer, List<Integer>> bizFileMap = fileBizService.get(tableName);
        if (MapUtil.isNotEmpty(bizFileMap)) {
            List<Integer> fileIdList = bizFileMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(fileIdList)) {
                List<FileEntity> list = baseRepository.listByIds(fileIdList);
                if (CollUtil.isNotEmpty(list)) {
                    Map<Integer, List<FileEntity>> map = new HashMap<>();
                    for (Map.Entry<Integer, List<Integer>> entry : bizFileMap.entrySet()) {
                        Integer bizId = entry.getKey();
                        if (bizIdList.contains(bizId)) {
                            List<Integer> fileList = entry.getValue();
                            List<FileEntity> collect = list.stream().filter(fileEntity -> fileList.contains(fileEntity.getId())).collect(Collectors.toList());
                            if (CollUtil.isNotEmpty(collect)) {
                                map.put(bizId, collect);
                            }

                        }
                    }
                    return map;
                }
            }
        }
        return Map.of();
    }

    private String getTableName(Class<?> clazz) {
        // 获取类上的注解值
        TableName annotation = AnnotationUtil.getAnnotation(clazz, TableName.class);
        if (annotation == null) {
            throw new RuntimeException("类上没有TableName注解");
        }
        return annotation.value();
    }

}
