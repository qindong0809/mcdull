package io.gitee.dqcer.blaze.domain.form;

import io.gitee.dqcer.mcdull.framework.base.support.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 人才表 新建表单
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */

@Data
public class TalentAddDTO implements DTO {

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "姓名 不能为空")
    private String name;

    @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "身份证号 不能为空")
    private String idNumber;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "联系电话 不能为空")
    private String contactNumber;

    @Schema(description = "工作单位性质 1、私企 2、国企 3、其它", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工作单位性质 1、私企 2、国企 3、其它 不能为空")
    private Integer workUnitType;

    @Schema(description = "社保状态 1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一 ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "社保状态 1、无社保 2、唯一社保可转 3、唯一社保可停 4、国企社保非唯一 5、私企社保非唯一  不能为空")
    private Integer socialSecurityStatus;

    @Schema(description = "社保所在省", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "社保所在省 不能为空")
    private String provincesCode;

    @Schema(description = "社保所在市", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "社保所在市 不能为空")
    private String cityCode;

    @Schema(description = "性别 0、未知 1、男 2、女 9、不明", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "性别 0、未知 1、男 2、女 9、不明 不能为空")
    private Integer gender;

    @Schema(description = "职称 1/无 2/初级 3/中级 4/高级 5/不限", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "职称 1/无 2/初级 3/中级 4/高级 5/不限 不能为空")
    private Integer title;

    @NotNull
    private Integer responsibleUserId;

}