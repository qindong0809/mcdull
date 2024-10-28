package io.gitee.dqcer.mcdull.uac.provider.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.IdEntity;
import io.gitee.dqcer.mcdull.framework.base.entity.RelEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * biz audit
 *
 * @author dqcer
 * @since 2022/11/07
 */
@Getter
@Setter
@TableName("sys_biz_audit")
public class BizAuditEntity extends IdEntity<Integer> {

    private static final long serialVersionUID = 1L;

    private String bizTypeCode;

    /**
     * {@link io.gitee.dqcer.mcdull.business.common.audit.OperationTypeEnum}
     */
    private Integer operation;

    private String bizIndex;

    private Integer bizId;

    private String comment;

    private String operator;

    private Date operationTime;

    private String ext;
}
