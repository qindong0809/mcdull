package io.gitee.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;

/**
 * 自定义属性
 *
 * @author dqcer
 * @since 2024/03/26
 */
@TableName("sys_custom_property")
@Data
public class CustomPropertyEntity extends BaseEntity {

    private String code;

    private String name;

    private String propertyValue;

    private String remark;
}
