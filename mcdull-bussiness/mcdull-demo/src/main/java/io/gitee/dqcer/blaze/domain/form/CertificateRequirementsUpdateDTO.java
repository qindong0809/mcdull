package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 证书需求表 更新表单
 *
 * @author dqcer
 * @since 2025-01-07 21:32:34
 */

@Data
public class CertificateRequirementsUpdateDTO implements DTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id 不能为空")
    private Integer id;

    @Schema(description = "证书级别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书级别 不能为空")
    private Integer certificateLevel;

    @Schema(description = "专业", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "专业 不能为空")
    private Integer specialty;

    @Schema(description = "单位所在省", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单位所在省 不能为空")
    private Integer province;

    @Schema(description = "单位所在市", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单位所在市 不能为空")
    private Integer city;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量 不能为空")
    private Integer quantity;

    @Schema(description = "职称 1/无 2/初级 3/中级 4/高级 5/不限", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "职称 1/无 2/初级 3/中级 4/高级 5/不限 不能为空")
    private String title;

    @Schema(description = "初始/转注（1/无 2/初始 3/转注 4/其它）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "初始/转注（1/无 2/初始 3/转注 4/其它） 不能为空")
    private Integer initialOrTransfer;

    @Schema(description = "证书状态（1/正常 2/不正常）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "证书状态（1/正常 2/不正常） 不能为空")
    private Integer certificateStatus;

    @Schema(description = "职位合同价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位合同价 不能为空")
    private BigDecimal positionContractPrice;

    @Schema(description = "其他费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "其他费用 不能为空")
    private BigDecimal otherCosts;

    @Schema(description = "职位实际价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位实际价 不能为空")
    private BigDecimal actualPositionPrice;

    @Schema(description = "期限（月）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "期限（月） 不能为空")
    private Integer duration;

    @Schema(description = "招标出场（1、可出场不招标 2、不出场可招标 3、出场招标 4、项目 5、资质 6、其它情况）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "招标出场（1、可出场不招标 2、不出场可招标 3、出场招标 4、项目 5、资质 6、其它情况） 不能为空")
    private Integer biddingExit;

    @Schema(description = "三类人员（1、无 2、有A证 3、有B证 4、有C证 5、可考A证 6、可考B证 7、可考C证 8、不考A证 9、不考B证 10、不考C证 11、其它）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "三类人员（1、无 2、有A证 3、有B证 4、有C证 5、可考A证 6、可考B证 7、可考C证 8、不考A证 9、不考B证 10、不考C证 11、其它） 不能为空")
    private Integer threePersonnel;

    @Schema(description = "社保要求（1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "社保要求（1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ） 不能为空")
    private Integer socialSecurityRequirement;

    @Schema(description = "职位来源（1、企业直签 2、同行中介）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职位来源（1、企业直签 2、同行中介） 不能为空")
    private Integer positionSource;

    @Schema(description = "职位标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "职位标题 不能为空")
    private String positionTitle;

    @Schema(description = "备注")
    private String remarks;

}