package io.gitee.dqcer.blaze.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.dqcer.mcdull.framework.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("blaze_order_detail")
public class BlazeOrderDetailEntity extends BaseEntity<Integer> implements Approve{


    private Integer blazeOrderId;

    private Boolean isTalent;


    private Date operationTime;

    private BigDecimal price;

    /**
     * 备注
     */
    private String remarks;

    private Integer approve;

    private String approveRemarks;

    private Integer responsibleUserId;

}