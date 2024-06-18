package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys_form_item
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_form_item")
public class FormItemEntity extends RelEntity<Integer> {

    private Integer formId;

    private String controlType;

    private String label;

    private String labelCode;

    private String options;

    private Boolean required;

    private Integer orderNumber;
}