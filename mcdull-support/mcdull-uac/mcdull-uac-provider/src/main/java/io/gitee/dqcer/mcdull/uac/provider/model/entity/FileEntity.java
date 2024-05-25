package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * sys file
 *
 * @author dqcer
 * @since 2022/11/07
 */
@TableName("sys_file")
@Getter
@Setter
public class FileEntity extends BaseEntity<Integer> {

    private Integer folderType;

    private String fileName;

    private Integer fileSize;

    private String fileKey;

    private String fileType;
}