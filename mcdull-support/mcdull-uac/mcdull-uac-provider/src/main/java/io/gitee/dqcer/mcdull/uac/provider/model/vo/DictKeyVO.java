package io.gitee.dqcer.mcdull.uac.provider.model.vo;

import io.gitee.dqcer.mcdull.framework.base.support.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典钥匙视图对象
 * 字典
 *
 * @author dqcer
 * @since 2024/04/28
 */
@Data
public class DictKeyVO implements VO {

    @Schema
    private Integer dictKeyId;

    @Schema
    private String keyCode;

    @Schema
    private String keyName;

    @Schema
    private String remark;
}