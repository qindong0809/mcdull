package io.gitee.dqcer.blaze.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateFormat;
import io.gitee.dqcer.mcdull.framework.web.json.serialize.DynamicDateSerialize;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.IArea;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 企业信息 列表VO
 *
 * @author dqcer
 * @since 2024-06-24 22:28:36
 */

@Data
public class CustomerInfoVO implements IArea, VO {

    private Integer id;

    @Schema(description = "企业名称")
    private String name;

    @Schema(description = "所在地省代码")
    private String provincesCode;

    @Schema(description = "所在地省名称")
    private String provincesName;

    @Schema(description = "所在市代码")
    private String cityCode;

    @Schema(description = "所在市名称")
    private String cityName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String phoneNumber;

    @Schema(description = "客户类型")
    private String customerType;

    @Schema(description = "客户类型名称")
    private String customerTypeName;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "创建人名称")
    private String createdName;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新人")
    private Integer updatedBy;

    @Schema(description = "更新人名称")
    private String updatedName;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "状态")
    private Boolean inactive;

    @Schema(description = "状态名称")
    private String inactiveName;

    private Integer responsibleUserId;

    private String responsibleUserIdStr;

}