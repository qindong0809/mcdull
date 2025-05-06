package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import lombok.Data;

/**
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderSearchDTO implements DTO {

    private Integer talentCertId;

    private Integer customerCertId;

    private Integer orderId;

}