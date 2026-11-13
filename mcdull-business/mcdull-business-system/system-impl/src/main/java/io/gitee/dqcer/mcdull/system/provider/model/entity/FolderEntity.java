package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件夹实体
 *
 * @author dqcer
 * @since 2024/11/27
 */
@TableName("sys_folder")
@Getter
@Setter
public class FolderEntity extends BaseEntity<Integer> {

    private String name;

    private Integer parentId;

    private String idPath;

    private Integer sort;
}