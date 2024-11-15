package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.TimestampEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * biz audit field entity
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_biz_audit_field")
public class BizAuditFieldEntity extends TimestampEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private Integer bizAuditId;

    private String fieldName;

    private Integer sortOrder;

    private String oldValue;

    private String newValue;
}
