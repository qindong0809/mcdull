package io.gitee.dqcer.mcdull.uac.provider.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

/**
 * 文件夹插入 DTO
 *
 * @author dqcer
 * @since 2024/11/29
 */
@Data
public class FolderInsertDTO implements DTO {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer parentId;

    private Integer sort;
}