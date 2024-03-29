package io.gitee.dqcer.mcdull.admin.model.entity.common;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;

/**
 * 文件
 *
 * @author dqcer
 * @since 2023/03/11
 */
@Data
@TableName("sys_file")
public class SysFileEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 路径
     */
    private String path;

    /**
     * url
     */
    private String url;

    /**
     * 唯一文件名
     */
    private String uniqueFileName;

    /**
     * 文件md5
     */
    private String fileMd5;

    /**
     * 原始文件名字
     */
    private String originalFileName;

    /**
     * 文件大小
     */
    private Long fileSize;

}
