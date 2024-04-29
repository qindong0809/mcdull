package io.gitee.dqcer.mcdull.uac.provider.model.convert;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.ConfigAddDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.ConfigEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.entity.FileEntity;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ConfigVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileDownloadVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileVO;

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



    public static ConfigVO convertToConfigVO(ConfigEntity item){
        if (item == null){
            return null;
        }
        ConfigVO vo = new ConfigVO();
        vo.setId(item.getId());
        vo.setConfigName(item.getConfigName());
        vo.setConfigKey(item.getConfigKey());
        vo.setConfigValue(item.getConfigValue());
        vo.setRemark(item.getRemark());
        vo.setUpdateTime(item.getUpdatedTime());
        vo.setCreateTime(item.getCreatedTime());

        return vo;
    }
    private FileConvert() {
    }
}