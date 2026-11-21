package io.gitee.dqcer.mcdull.system.facade.model.dto;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.util.List;

/**
 * 业务文件查询/删除入参。
 * <p>
 * {@code bizType} 为业务表的 {@code @TableName} 值（如 talent_certificate），
 * 替代原契约中不可跨服务传递的 {@code Class<?>} 参数。
 */
@Data
public class FileBizDTO implements DTO {

    /**
     * 业务表表名
     */
    private String bizType;

    /**
     * 业务 id
     */
    private Integer bizId;

    /**
     * 业务 id 列表（批量查询文件用）
     */
    private List<Integer> bizIdList;

    /**
     * 待删除的文件 id 列表
     */
    private List<Integer> deleteFileIdList;
}
