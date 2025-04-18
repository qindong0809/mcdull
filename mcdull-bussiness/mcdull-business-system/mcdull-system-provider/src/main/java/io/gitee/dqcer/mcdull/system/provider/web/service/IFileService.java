package io.gitee.dqcer.mcdull.system.provider.web.service;


import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * File Service
 *
 * @author dqcer
 * @since 2024/7/25 9:22
 */

public interface IFileService {

    PagedVO<FileVO> queryPage(FileQueryDTO dto);

    FileUploadVO fileUpload(MultipartFile file, Integer folder);

    void batchFileUpload(List<MultipartFile> fileList, Integer bizId, Class<?> clazz, List<Integer> deleteFileIdList);

    FileUploadVO fileUpload(File file, Integer folder);

    String getFileUrl(String fileKey);

    FileDownloadVO getDownloadFile(String fileKey);

    List<FileVO> getFileList(List<String> fileKeyList);

    Map<Integer, FileEntity> map(Set<Integer> fileIdSet);

    void remove(Integer bizId, Class<?> clazz);

    Map<Integer, List<FileEntity>> get(List<Integer> idList, Class<?> aClass);
}
