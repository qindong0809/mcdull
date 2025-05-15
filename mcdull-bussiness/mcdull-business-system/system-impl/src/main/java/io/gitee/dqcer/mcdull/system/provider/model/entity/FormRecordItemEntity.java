package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys_form_record_item
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_form_record_item")
public class FormRecordItemEntity extends RelEntity<Integer> {

    private Integer formId;

    private Integer formItemId;

    private Integer formRecordId;

    private String currentValue;
}