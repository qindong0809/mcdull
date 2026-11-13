package io.gitee.dqcer.mcdull.blaze.domain.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 证书需求表 列表VO
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

@Data
public class CustomerTalentCertVO implements VO {

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "客户id")
    private Integer customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "职位标题")
    private String positionTitle;



}