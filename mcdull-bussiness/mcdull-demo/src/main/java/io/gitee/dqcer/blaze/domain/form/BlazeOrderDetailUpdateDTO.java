package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderDetailUpdateDTO implements DTO {

    private Integer id;

    private Integer responsibleUserId;

    private Date operationTime;

    private String operationTimeStr;

    private BigDecimal price;

    private String remarks;

    private List<Integer> deleteFileIdList;


}