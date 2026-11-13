package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户过滤实体
 *
 * @author dqcer
 * @since 2026/02/06
 */
@TableName("sys_user_filter")
@Getter
@Setter
public class UserFilterEntity extends BaseEntity<Integer> {

    private Integer userId;
    private Integer roleId;
    private String categoryCode;
    private String subCategoryCode;
    private String filterTitle;
    private String filterContent;
}
