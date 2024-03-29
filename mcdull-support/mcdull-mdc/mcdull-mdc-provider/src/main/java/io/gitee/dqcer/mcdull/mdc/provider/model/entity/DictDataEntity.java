package io.gitee.dqcer.mcdull.mdc.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dqcer
 * @since 2022/11/01 22:11:06
 */
@TableName("sys_dict_data")
@Data
@EqualsAndHashCode(callSuper = true)
public class DictDataEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer dictSort;

    private String dictLabel;

    private String dictValue;

    private String dictType;

    private String remark;

}
