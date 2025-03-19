package io.gitee.dqcer.mcdull.uac.provider.web.service;


import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.FileQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileUploadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;
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

    FileUploadVO fileUpload(MultipartFile file, Integer folder, Integer bizId, String bizCode);

    FileUploadVO fileUpload(File file, Integer folder);

    String getFileUrl(String fileKey);

    FileDownloadVO getDownloadFile(String fileKey, String userAgent);

    List<FileVO> getFileList(List<String> fileKeyList);

    void removeByFileId(Integer fileId, Integer bizId, String bizCode);

    Map<Integer, FileEntity> map(Set<Integer> fileIdSet);

    void remove(Integer bizId, String bizCode);
}
