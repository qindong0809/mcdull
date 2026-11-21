package io.gitee.dqcer.mcdull.system.provider.web.def;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.URLUtil;
import io.gitee.dqcer.mcdull.system.facade.model.dto.FileBizDTO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileBinaryVO;
import io.gitee.dqcer.mcdull.system.facade.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.system.facade.service.FileRemoteApi;
import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.web.manager.ICommonManager;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFileService;
import io.gitee.dqcer.mcdull.system.provider.web.service.IFolderService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件远程能力实现：同时作为本地 {@code @Primary} bean 与 HTTP 服务端点。
 */
@Primary
@RestController
public class FileRemoteApiDefImpl implements FileRemoteApi {

    @Resource
    private IFileService fileService;
    @Resource
    private IFolderService folderService;
    @Resource
    private ICommonManager commonManager;

    @Override
    public void batchUpload(FileBizDTO meta, List<MultipartFile> files) {
        fileService.batchUploadByBizType(files, meta.getBizId(), meta.getBizType(), meta.getDeleteFileIdList());
    }

    @Override
    public void remove(FileBizDTO dto) {
        fileService.removeByBizType(dto.getBizId(), dto.getBizType());
    }

    @Override
    public Map<Integer, List<FileSimpleVO>> listFiles(FileBizDTO dto) {
        Map<Integer, List<FileEntity>> entityMap = fileService.getByBizType(dto.getBizIdList(), dto.getBizType());
        Map<Integer, List<FileSimpleVO>> result = new HashMap<>();
        if (CollUtil.isEmpty(entityMap)) {
            return result;
        }
        String fileDomainName = commonManager.getConfig("file-domain-name");
        entityMap.forEach((bizId, entityList) -> {
            List<FileSimpleVO> voList = new ArrayList<>();
            for (FileEntity entity : entityList) {
                FileSimpleVO vo = new FileSimpleVO();
                vo.setId(entity.getId());
                vo.setFileName(entity.getFileName());
                vo.setUrl(URLUtil.encodeBlank(fileDomainName + "/upload/" + entity.getFileKey()));
                voList.add(vo);
            }
            result.put(bizId, voList);
        });
        return result;
    }

    @Override
    public List<FileBinaryVO> downloadFiles(FileBizDTO dto) {
        List<FileBinaryVO> result = new ArrayList<>();
        Map<Integer, List<FileEntity>> entityMap = fileService.getByBizType(List.of(dto.getBizId()), dto.getBizType());
        List<FileEntity> entityList = entityMap.get(dto.getBizId());
        if (CollUtil.isEmpty(entityList)) {
            return result;
        }
        for (FileEntity entity : entityList) {
            FileBinaryVO vo = new FileBinaryVO();
            vo.setFileName(entity.getFileName());
            vo.setContent(fileService.getDownloadFile(entity.getFileKey()).getValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public void uploadToSystemFolder(MultipartFile file) {
        fileService.fileUpload(file, folderService.getSystemFolderId());
    }
}
