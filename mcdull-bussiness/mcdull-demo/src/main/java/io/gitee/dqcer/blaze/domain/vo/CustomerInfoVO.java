package io.gitee.dqcer.blaze.domain.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
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
public class CustomerInfoVO implements VO {

    private Integer id;

    @Schema(description = "企业名称")
    private String name;

    @Schema(description = "所在地省代码")
    private String provincesCode;

    @Schema(description = "所在市代码")
    private String cityCode;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String phoneNumber;

    @Schema(description = "客户类型")
    private String customerType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private Integer createdBy;

    @Schema(description = "创建时间")
    private Date createdTime;

    @Schema(description = "更新人")
    private Integer updatedBy;

    @Schema(description = "更新时间")
    private Date updatedTime;

    @Schema(description = "状态")
    private Boolean inactive;

}