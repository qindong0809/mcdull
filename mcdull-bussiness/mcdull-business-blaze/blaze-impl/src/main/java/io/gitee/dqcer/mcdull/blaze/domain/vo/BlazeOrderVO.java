package io.gitee.dqcer.mcdull.blaze.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.system.provider.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.IFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单合同 列表VO
 *
 * @author dqcer
 * @since 2025-01-18 11:33:31
 */

@Data
public class BlazeOrderVO extends ApproveVO implements IFileVO, VO {

    private Integer id;

    private String orderNo;

    @Schema(description = "所属人才证书")
    private Integer talentCertId;

    private String talentCertName;

    private String talentPayment;

    private String nowTalentPayment;

    @Schema(description = "所属企业证书")
    private Integer customerCertId;

    private String customerCertName;

    private String enterpriseCollection;

    private String nowEnterpriseCollection;

    private String performance;

    @Schema(description = "合同时间")
    private Date contractTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date contractTimeStr;

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

    private Date startDate;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date startDateStr;

    private Date endDate;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    private Date endDateStr;

    @Schema(description = "剩余日期")
    private String daysUntilDue;

    private Boolean firstIsTalent;

    @Schema(description = "附件列表")
    private List<FileSimpleVO> fileList;

    private Integer talentResponsibleUserId;

    @Schema(description = "人才负责人")
    private String talentResponsibleUserIdStr;

    private String talentResponsibleUserPhone;

    private Integer talentResponsibleDepartmentId;

    private String talentResponsibleDepartment;

    private Integer customerResponsibleUserId;

    @Schema(description = "企业负责人")
    private String customerResponsibleUserIdStr;

    private String customerResponsibleUserPhone;

    private Integer customerResponsibleDepartmentId;

    private String customerResponsibleDepartment;
}