package io.gitee.dqcer.mcdull.system.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys_form_record
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_form_record")
public class FormRecordEntity extends BaseEntity<Integer> {

    private Integer formId;
}