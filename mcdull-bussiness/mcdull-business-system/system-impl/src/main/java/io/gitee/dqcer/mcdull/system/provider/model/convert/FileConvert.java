package io.gitee.dqcer.mcdull.system.provider.model.convert;

import io.gitee.dqcer.mcdull.system.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileVO;

/**
* 系统配置 对象转换工具类
*
* @author dqcer
* @since 2024-04-29
*/
public class FileConvert {


    public static FileVO convertToEntity(FileEntity item){
        FileVO fileVO = new FileVO();
        fileVO.setFileId(item.getId());
        fileVO.setFolderType(item.getFolderType());
        fileVO.setFileName(item.getFileName());
        fileVO.setFileSize(item.getFileSize());
        fileVO.setFileType(item.getFileType());
        fileVO.setFileKey(item.getFileKey());
        fileVO.setCreateTime(item.getCreatedTime());
        return fileVO;
    }

    private FileConvert() {
    }
}