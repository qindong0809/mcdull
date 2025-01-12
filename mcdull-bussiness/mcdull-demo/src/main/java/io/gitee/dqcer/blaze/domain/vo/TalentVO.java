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
 * 人才表 列表VO
 *
 * @author dqcer
 * @since 2025-01-10 19:52:20
 */

@Data
public class TalentVO implements IArea, VO {


    @Schema(description = "id")
    private Integer id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "身份证号")
    private String idNumber;

    @Schema(description = "联系电话")
    private String contactNumber;

    /**
     * {@link  io.gitee.dqcer.blaze.domain.enums.TalentWorkUnitTypeEnum}
     */
    @Schema(description = "工作单位性质")
    private Integer workUnitType;

    @Schema(description = "工作单位")
    private String workUnitTypeName;

    @Schema(description = "社保状态")
    private Integer socialSecurityStatus;

    @Schema(description = "社保状态名称")
    private String socialSecurityStatusName;

    @Schema(description = "所在地省代码")
    private String provincesCode;

    @Schema(description = "所在地省名称")
    private String provincesName;

    @Schema(description = "所在市代码")
    private String cityCode;

    @Schema(description = "所在市名称")
    private String cityName;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "性别")
    private String genderName;

    @Schema(description = "职称 1/无 2/初级 3/中级 4/高级 5/不限")
    private Integer title;

    @Schema(description = "职称名称")
    private String titleName;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "创建时间")
    private Date createdTime;

    @DynamicDateFormat(enableTimezone = true)
    @JsonSerialize(using = DynamicDateSerialize.class)
    @Schema(description = "更新时间")
    private Date updatedTime;

}