package io.gitee.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys dict实体
 *
 * @author dqcer
 * @since 2022/11/01 22:11:06
 */
@TableName("sys_dict_type")
@Data
@EqualsAndHashCode(callSuper = true)
public class DictTypeEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String dictName;

    private String dictType;

    private String remark;

}
