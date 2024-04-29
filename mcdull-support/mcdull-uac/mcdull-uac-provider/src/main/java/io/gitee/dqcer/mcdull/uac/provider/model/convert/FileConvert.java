package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;

import java.time.LocalDateTime;

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
        return fileVO;
    }

    private FileConvert() {
    }
}