package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import lombok.Data;

/**
 * 文件元数据
 *
 */
@Data
public class FileMetadataVO {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小/字节
     */
    private Long fileSize;

    /**
     * 文件格式
     */
    private String fileFormat;
}
