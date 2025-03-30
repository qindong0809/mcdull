package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单合同 新建表单
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderDetailAddDTO implements DTO {

    private Integer blazeOrderId;

    private Boolean isTalent;

    private Integer responsibleUserId;

    private Date operationTime;

    private BigDecimal price;

    private String remarks;


}