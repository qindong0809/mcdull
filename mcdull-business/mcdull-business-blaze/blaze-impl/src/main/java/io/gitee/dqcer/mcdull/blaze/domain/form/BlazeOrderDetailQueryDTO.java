package io.gitee.dqcer.mcdull.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.dto.PagedDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlazeOrderDetailQueryDTO extends PagedDTO implements PermissionDTO {

    @NotNull
    private Boolean isTalent;

    private Integer responsibleUserId;

    private Date operationTime;

    private BigDecimal price;

    private List<Integer> orderIdList;

    private List<Integer> responsibleUserIdList;

}