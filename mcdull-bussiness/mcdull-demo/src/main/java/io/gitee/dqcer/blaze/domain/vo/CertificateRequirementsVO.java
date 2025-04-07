package io.gitee.dqcer.blaze.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.ApproveVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.FileSimpleVO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.IArea;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.IFileVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 证书需求表 列表VO
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CertificateRequirementsVO extends ApproveVO implements IFileVO, IArea, VO {


    @Schema(description = "id")
    private Integer id;

    @Schema(description = "客户id")
    private Integer customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "证书级别")
    private Integer certificateLevel;

    @Schema(description = "证书级别名称")
    private String certificateLevelName;

    @Schema(description = "专业")
    private Integer specialty;

    @Schema(description = "专业名称")
    private String specialtyName;

    @Schema(description = "所在地省代码")
    private String provincesCode;

    @Schema(description = "所在地省名称")
    private String provincesName;

    @Schema(description = "所在市代码")
    private String cityCode;

    @Schema(description = "所在市名称")
    private String cityName;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "职称")
    private Integer title;

    @Schema(description = "职称名称")
    private String titleName;

    @Schema(description = "初始/转注")
    private Integer initialOrTransfer;

    @Schema(description = "初始/转注名称")
    private String initialOrTransferName;

    @Schema(description = "证书状态")
    private Integer certificateStatus;

    @Schema(description = "证书状态名称")
    private String certificateStatusName;

    @Schema(description = "职位合同价")
    private String positionContractPrice;

    @Schema(description = "其他费用")
    private String otherCosts;

    @Schema(description = "职位实际价")
    private String actualPositionPrice;

    @Schema(description = "期限（月）")
    private Integer duration;

    @Schema(description = "招标出场")
    private Integer biddingExit;

    @Schema(description = "招标出场名称")
    private String biddingExitName;

    @Schema(description = "三类人员")
    private Integer threePersonnel;

    @Schema(description = "三类人员名称")
    private String threePersonnelName;

    @Schema(description = "社保要求")
    private Integer socialSecurityRequirement;

    @Schema(description = "社保要求名称")
    private String socialSecurityRequirementName;

    @Schema(description = "职位来源")
    private Integer positionSource;

    @Schema(description = "职位来源名称")
    private String positionSourceName;

    @Schema(description = "职位标题")
    private String positionTitle;

    @Schema(description = "备注")
    private String remarks;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

    private List<FileSimpleVO> fileList;

}