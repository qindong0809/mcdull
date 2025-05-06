package io.gitee.dqcer.mcdull.blaze.domain.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import lombok.Data;

/**
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderDetailListVO implements VO {

    private Integer blazeOrderId;

    private String orderNo;

    private String talentCertTitle;

    private String customerCertTitle;

    private Boolean isTalent;

}