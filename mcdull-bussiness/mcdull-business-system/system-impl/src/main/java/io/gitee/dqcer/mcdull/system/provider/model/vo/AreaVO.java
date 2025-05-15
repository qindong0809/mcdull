package io.gitee.dqcer.mcdull.system.provider.model.vo;


import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Area 列表VO
 *
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */

@Data
public class AreaVO implements VO {


    @Schema(description = "区域编码")
    private String code;

    @Schema(description = "地名简称")
    private String name;

    @Schema(description = "全称")
    private String fullname;

    @Schema(description = "邮政编码")
    private String govcode;

    @Schema(description = "维度")
    private BigDecimal lat;

    @Schema(description = "经度")
    private BigDecimal lng;

}
