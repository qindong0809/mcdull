package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sys_form
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_form")
public class FormEntity extends BaseEntity<Integer> {

    private String name;

    private String jsonText;

    private Boolean publish;

    private String remark;

}