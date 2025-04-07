package io.gitee.dqcer.blaze.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.IFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 订单合同 列表VO
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BlazeOrderDetailVO extends ApproveVO implements IFileVO, VO {

    private Integer id;

    private Integer blazeOrderId;

    private String orderNo;

    private String talentCertTitle;

    private String customerCertTitle;

    private Boolean isTalent;

    private Integer responsibleUserId;

    private String responsibleUserName;

    private Date operationTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date operationTimeStr;

    private String price;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "创建人")
    private Integer createdBy;

    private String createdByStr;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新人")
    private Integer updatedBy;

    private String updatedByStr;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "状态（true/已失活 false/未失活）")
    private Boolean inactive;

    @Schema(description = "状态（true/已失活 false/未失活）")
    private String inactiveStr;

    private List<FileSimpleVO> fileList;
}