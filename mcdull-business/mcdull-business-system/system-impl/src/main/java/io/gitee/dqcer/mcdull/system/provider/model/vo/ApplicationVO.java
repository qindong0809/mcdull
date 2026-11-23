package io.gitee.dqcer.mcdull.system.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用视图对象
 *
 * @author dqcer
 * @since 2026/11/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationVO implements VO {

    @Schema(description = "应用编码")
    private String code;

    @Schema(description = "应用名称")
    private String name;
}
